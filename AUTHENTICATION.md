# Autenticación JWT para la API de clínicas

El repositorio contiene dos aplicaciones independientes:

- Clínica: puerto `8080`, base `clinica`.
- Autenticación: puerto `8081`, base `auth_db`.

El servicio de autenticación emite JWT `RS256`. La llave privada se queda en el servicio auth; el backend clínico solo necesita la pública. Ambos leen llaves PEM PKCS#8/X.509 sin incluir credenciales ni llaves en Git.

## Preparación

Requisitos: Java 21, Maven (o wrapper del proyecto clínico), MySQL y OpenSSL. Crear ambas bases (`clinica` y `auth_db`) en el servidor MySQL. Desde `auth-service/` generar el par:

```sh
./scripts/generate-keys.sh
```

Usar el mismo par en el servicio auth y proveer `auth-service/keys/public.pem` al backend clínico mediante volumen seguro o ruta local. No copiar `private.pem` a clínicas. Si se rota una llave, coordinar el cambio para aceptar tokens firmados por el par vigente.

## Ejecutar

En una terminal desde `auth-service/`:

```sh
AUTH_DB_URL='jdbc:mysql://localhost:3307/auth_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \
AUTH_DB_USERNAME=root AUTH_DB_PASSWORD='tu-clave' \
JWT_PRIVATE_KEY=file:./keys/private.pem JWT_PUBLIC_KEY=file:./keys/public.pem \
mvn spring-boot:run
```

En otra terminal, desde la raíz:

```sh
DB_URL='jdbc:mysql://localhost:3307/clinica?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \
DB_USERNAME=root DB_PASSWORD='tu-clave' \
JWT_PUBLIC_KEY_LOCATION=file:./auth-service/keys/public.pem \
./mvnw spring-boot:run
```

`JWT_PUBLIC_KEY_LOCATION` debe apuntar a un archivo PEM `BEGIN PUBLIC KEY`. En Docker, montar las llaves como secretos de solo lectura y establecer la ubicación `file:/...` dentro del contenedor. No hay credenciales predeterminadas.

## Pruebas manuales

Registrar un usuario (se asigna `ROLE_USER` y se devuelve un token):

```sh
curl -i http://localhost:8081/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo","password":"ClaveSegura123"}'
```

Obtener otro token:

```sh
curl -i http://localhost:8081/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo","password":"ClaveSegura123"}'
```

Con el `accessToken` de la respuesta, llamar un endpoint clínico protegido:

```sh
curl -i http://localhost:8080/api/paciente \
  -H 'Authorization: Bearer <accessToken>'
```

Las rutas de clínica requieren JWT válido; sin token o con token inválido responden `401`. El servicio auth publica su llave pública en `GET /api/auth/jwks.json` y permite verificar su propio token en `GET /api/auth/me` con Bearer token. El endpoint JWKS es útil para clientes; el resource server de clínicas usa el PEM configurado para verificación local.

Las cuentas registradas públicamente reciben solo `ROLE_USER`. No se expone una ruta pública para asignar roles administrativos.
