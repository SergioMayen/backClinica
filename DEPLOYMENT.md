# Despliegue

La rama de despliegue se llama `despliegue-seguridad`. Render toma de ella los dos servicios Docker Java 21 y el Static Site Angular. La salida Angular es `frontend/dist/clinica/browser`; las rutas del navegador se reescriben a `index.html`.

## PostgreSQL administrado con Neon

La aplicación y el servicio de autenticación usan PostgreSQL en dos bases separadas: `clinica` y `auth_db`. Crea un proyecto Neon gratuito y, dentro de su rama principal, crea ambas bases. El plan gratuito publicado incluye 1 GB de almacenamiento y 100 horas de cómputo por proyecto al mes; el cómputo escala a cero después de 5 minutos inactivo y vuelve a arrancar al recibir consultas. Al agotar las cuotas, el cómputo queda suspendido hasta el siguiente periodo; no habilites un plan de pago para esta demostración. Revisa los límites vigentes en https://neon.com/pricing.

En Neon, usa **Connect** para copiar la cadena de conexión de cada base. Elige la conexión con pooler para Render. Cambia el prefijo `postgresql://` por `jdbc:postgresql://` y conserva el host, puerto, base, usuario, contraseña y parámetros TLS que proporciona Neon. La forma será similar a:

```text
jdbc:postgresql://<host-pooler-neon>:5432/clinica?sslmode=require
```

No pongas credenciales reales en archivos ni en Git. Configura estos valores en Render como secretos:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` para `backclinica-api` (base `clinica`).
- `AUTH_DB_URL`, `AUTH_DB_USERNAME`, `AUTH_DB_PASSWORD` para `backclinica-auth` (base `auth_db`).

Usa una contraseña fuerte generada por Neon y guarda una copia en un gestor de contraseñas. No uses información real de pacientes para la demostración. Hibernate actualiza el esquema al iniciar (`ddl-auto=update`); no se ejecuta ningún script que borre o reemplace datos.

## Claves RSA y configuración de Render

Genera un par RSA para la aplicación:

```sh
cd auth-service
./scripts/generate-keys.sh
```

Carga `auth-service/keys/private.pem` y `auth-service/keys/public.pem` como archivos secretos del servicio `backclinica-auth`, en `/etc/secrets/private.pem` y `/etc/secrets/public.pem`. Carga únicamente `public.pem` en `backclinica-api`, en `/etc/secrets/public.pem`. Nunca agregues `keys/` o archivos PEM a Git. Render debe enlazar los tres servicios con la rama `despliegue-seguridad`. Si los subdominios previstos ya están ocupados, actualiza `API_URL`, `AUTH_URL` y `FRONTEND_ORIGIN` con los nombres que Render asigne.

## Demo

Después de desplegar, usa `POST /api/auth/register` para crear un usuario ficticio. Usa el token devuelto para crear un registro ficticio de paciente en `POST /api/pacientes`. La API no contiene semillas automáticas; no se modifican filas que ya existan. El frontend carga las URLs de las APIs desde `API_URL` y `AUTH_URL` al compilar.

### Cloudflare Turnstile para el login

El login solo acepta solicitudes con un token de Turnstile válido. Crea un widget en Cloudflare Turnstile, configura el hostname `backclinica-web.onrender.com` y acción `login`. En Render, configura `TURNSTILE_SITE_KEY` como variable de entorno del Static Site `backclinica-web`, y `TURNSTILE_SECRET_KEY` como secreto del Web Service `backclinica-auth`. `TURNSTILE_EXPECTED_HOSTNAME` debe ser `backclinica-web.onrender.com`. La clave secreta nunca se coloca en el frontend ni en Git. Después de guardar, espera los dos despliegues; el frontend debe recompilarse para incorporar la site key. Sin ambos valores, el formulario se mantiene bloqueado y la API rechaza el login de forma cerrada.

La API verifica el token con Cloudflare antes de consultar la cuenta o comprobar la contraseña. Rechaza tokens inválidos, expirados, con hostname distinto o acción distinta. Por eso un rechazo de Turnstile no suma un fallo de contraseña; la limitación de solicitudes se revisa por separado antes de validar Turnstile.
