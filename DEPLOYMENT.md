# Despliegue

La rama de despliegue se llama `despliegue-seguridad`. Render toma de ella los dos servicios Docker Java 21 y el Static Site Angular. La salida Angular es `frontend/dist/clinica/browser`; las rutas del navegador se reescriben a `index.html`.

## Azure SQL Database

El backend y el servicio de autenticación usan bases SQL Server separadas: `clinica` y `auth_db`. No se detectaron scripts de importación ni migraciones destructivas; Hibernate `update` conserva las tablas y filas existentes y crea o añade lo que falta. Para empezar de cero, crea dos bases de Azure SQL Database con la oferta gratuita aplicada y usa el comportamiento **auto-pause** al alcanzar el límite gratuito. La oferta publicada incluye 100 000 segundos vCore, 32 GB de datos y 32 GB de respaldo por base y mes; la base se pausa al agotar la cuota y no está disponible hasta el siguiente mes. Verifica que Azure muestre **Free offer applied** y costo estimado de USD 0 antes de crearla. La oferta gratuita no incluye SLA.

Al crear la base lógica y las dos bases de datos, asigna a cada servicio su propio usuario contenido, con permisos limitados a su base. El esquema se actualiza al iniciar la aplicación, así que esos usuarios deben poder crear y alterar tablas, además de leer y escribir los datos de su base. No uses datos reales de pacientes para la demostración.

Configura estas variables como secretos en Render:

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` para `backclinica-api`.
- `AUTH_DB_URL`, `AUTH_DB_USERNAME`, `AUTH_DB_PASSWORD` para `backclinica-auth`.

Formato de URL cifrada para Azure SQL:

```text
jdbc:sqlserver://<servidor>.database.windows.net:1433;databaseName=clinica;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30
```

En `AUTH_DB_URL`, cambia `databaseName=clinica` por `databaseName=auth_db`. Usa el nombre DNS completo del servidor y TLS con validación de certificado.

Azure SQL Database usa reglas de firewall IPv4. Mantén desactivada la opción para permitir todos los servicios de Azure y agrega solo los rangos de salida que Render muestre para `backclinica-api` y `backclinica-auth` en **Connect → Outbound**. Render comparte esos rangos por región; la lista exacta solo aparece después de crear los servicios. Mientras tanto, deja la lista de firewall vacía. No abras `0.0.0.0–255.255.255.255`.

## Claves RSA y configuración de Render

Genera un par RSA para la aplicación:

```sh
cd auth-service
./scripts/generate-keys.sh
```

Carga `auth-service/keys/private.pem` y `auth-service/keys/public.pem` como archivos secretos del servicio backclinica-auth, en `/etc/secrets/private.pem` y `/etc/secrets/public.pem`. Carga únicamente `public.pem` en backclinica-api, en `/etc/secrets/public.pem`. Nunca agregues `keys/` o archivos PEM a Git. Render debe enlazar los tres servicios con la rama `despliegue-seguridad`. Si los subdominios previstos ya están ocupados, actualiza `API_URL`, `AUTH_URL` y `FRONTEND_ORIGIN` con los nombres que Render asigne.

## Demo

Después de desplegar, usa `POST /api/auth/register` para crear un usuario ficticio. Usa el token devuelto para crear un registro ficticio de paciente en `POST /api/pacientes`. La API no contiene semillas automáticas; no se modifican filas que ya existan. El frontend carga las URLs de las APIs desde `API_URL` y `AUTH_URL` al compilar.
