# Despliegue

La rama de despliegue se llama `despliegue-seguridad`. Render toma de ella los dos servicios Docker Java 21 y el Static Site Angular. La salida Angular es `frontend/dist/clinica/browser`; las rutas del navegador se reescriben a `index.html`.

## Base MySQL

La aplicación usa MySQL en dos esquemas: `clinica` y `auth_db`. No hay un script que borre ni importe datos existentes; Hibernate `update` crea o añade las tablas y columnas necesarias. Configura dos bases y dos usuarios con permisos solo sobre su esquema. En producción configura las URLs JDBC con TLS, por ejemplo `jdbc:mysql://<servidor>:3306/clinica?sslMode=VERIFY_IDENTITY` y el esquema `auth_db` para autenticación. Añade las credenciales en los campos secretos de Render:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` para backclinica-api.
- `AUTH_DB_URL`, `AUTH_DB_USERNAME`, `AUTH_DB_PASSWORD` para backclinica-auth.

Si la cuenta Azure reúne los requisitos del nivel gratuito de Azure Database for MySQL Flexible Server, el límite vigente cubre hasta 750 horas y 32 GB durante 12 meses. El límite y la elegibilidad se deben confirmar en la suscripción antes de crear el recurso. Render PostgreSQL gratuito no sirve para esta aplicación y vence a los 30 días.

## Claves RSA y configuración de Render

Genera un par RSA para la aplicación:

```sh
cd auth-service
./scripts/generate-keys.sh
```

Carga `auth-service/keys/private.pem` y `auth-service/keys/public.pem` como archivos secretos del servicio backclinica-auth, en `/etc/secrets/private.pem` y `/etc/secrets/public.pem`. Carga únicamente `public.pem` en backclinica-api, en `/etc/secrets/public.pem`. Nunca agregues `keys/` o archivos PEM a Git. Render debe enlazar los tres servicios con la rama `despliegue-seguridad`. Si los subdominios previstos ya están ocupados, actualiza `API_URL`, `AUTH_URL` y `FRONTEND_ORIGIN` con los nombres que Render asigne.

La base Azure debe aceptar conexiones cifradas desde los rangos de salida que Render muestra en **Connect → Outbound** para cada servicio en Oregon. Añade esos rangos específicos al firewall de MySQL, nunca la regla general para cualquier servicio Azure. Las redes de salida gratuitas de Render son compartidas con otros servicios de la región.

## Demo

Después de desplegar, usa `POST /api/auth/register` para crear un usuario ficticio. Usa el token devuelto para crear un registro ficticio de paciente en `POST /api/pacientes`. La API no contiene semillas automáticas; no se modifican filas que ya existan. El frontend carga las URLs de las APIs desde `API_URL` y `AUTH_URL` al compilar.
