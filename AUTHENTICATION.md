# Autenticación JWT para la API de clínicas

El repositorio contiene dos aplicaciones independientes:

- Clínica: puerto `8080`, base `clinica`.
- Autenticación: puerto `8081`, base `auth_db`.

El servicio de autenticación emite JWT `RS256`. La llave privada se queda en el servicio auth; el backend clínico solo necesita la pública. Ambos leen llaves PEM PKCS#8/X.509 sin incluir credenciales ni llaves en Git.

## Preparación

Requisitos: Java 21, Maven (o wrapper del proyecto clínico), PostgreSQL y OpenSSL. Crear las bases `clinica` y `auth_db` en PostgreSQL. En producción se pueden alojar en el mismo proyecto Neon con una credencial del proyecto, pero cada servicio usa su propia base. Desde `auth-service/` generar el par:

```sh
./scripts/generate-keys.sh
```

Usar el mismo par en el servicio auth y proveer `auth-service/keys/public.pem` al backend clínico mediante volumen seguro o ruta local. No copiar `private.pem` a clínicas. Si se rota una llave, coordinar el cambio para aceptar tokens firmados por el par vigente.

## Ejecutar

En una terminal desde `auth-service/`:

```sh
AUTH_DB_URL='jdbc:postgresql://localhost:5432/auth_db' \
AUTH_DB_USERNAME='postgres' AUTH_DB_PASSWORD='valor-local-ficticio' \
JWT_PRIVATE_KEY=file:./keys/private.pem JWT_PUBLIC_KEY=file:./keys/public.pem \
mvn spring-boot:run
```

En otra terminal, desde la raíz:

```sh
DB_URL='jdbc:postgresql://localhost:5432/clinica' \
DB_USERNAME='postgres' DB_PASSWORD='valor-local-ficticio' \
JWT_PUBLIC_KEY_LOCATION=file:./auth-service/keys/public.pem \
./mvnw spring-boot:run
```

`JWT_PUBLIC_KEY_LOCATION` debe apuntar a un archivo PEM `BEGIN PUBLIC KEY`. En Docker, montar las llaves como secretos de solo lectura y establecer la ubicación `file:/...` dentro del contenedor. No hay credenciales predeterminadas.

Para Neon, usa el host pooler que muestra la consola y `sslmode=require` en cada JDBC URL. Guarda las URLs y contraseñas reales únicamente en los secretos del proveedor de despliegue.

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
  -d '{"username":"demo","password":"ClaveSegura123","turnstileToken":"<token-obtenido-del-widget>"}'
```

El endpoint `/api/auth/login` exige `turnstileToken` y lo valida contra Cloudflare Turnstile antes de verificar la contraseña. En local también debes usar un widget configurado para el hostname de desarrollo, su site key en el build y la secret key en el backend. Nunca sustituyas el valor por una clave secreta en el navegador o en este ejemplo. La clave de Turnstile es de un solo uso y expira; para una demostración, resuelve el widget en el formulario de login.

El rate limit de login admite 10 solicitudes por minuto por cliente y cuenta y devuelve `429` si se supera. Las respuestas del límite o de Turnstile no llegan al verificador de contraseña y no aumentan los cinco fallos consecutivos guardados por cuenta. Al quinto fallo se guarda un bloqueo de 15 minutos; el bloqueo rechaza la contraseña correcta y no se prolonga con nuevos intentos.

Con el `accessToken` de la respuesta, llamar un endpoint clínico protegido:

```sh
curl -i http://localhost:8080/api/paciente \
  -H 'Authorization: Bearer <accessToken>'
```

Las rutas de clínica requieren JWT válido; sin token o con token inválido responden `401`. El servicio auth publica su llave pública en `GET /api/auth/jwks.json` y permite verificar su propio token en `GET /api/auth/me` con Bearer token. El endpoint JWKS es útil para clientes; el resource server de clínicas usa el PEM configurado para verificación local.

Las cuentas registradas públicamente reciben solo `ROLE_USER`. No se expone una ruta pública para asignar roles administrativos.
