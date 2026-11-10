# Clínica · Frontend Angular

Aplicación Angular standalone para consumir los servicios Spring Boot del repositorio.

## Inicio local

1. Inicia SQL Server, el servicio de autenticación en `8081` y la API clínica en `8080` de acuerdo con [AUTHENTICATION.md](../AUTHENTICATION.md).
2. Desde `frontend/`, ejecuta `npm install` y `npm start`.
3. Abre `http://localhost:4200` e inicia sesión con una cuenta registrada en el servicio auth.

El proxy de Angular envía `/api/*` a la API clínica y `/auth/api/*` al servicio de autenticación. Así el navegador usa un mismo origen durante el desarrollo y no requiere habilitar CORS.

## Organización

- `src/app/core`: sesión JWT, interceptor y guards.
- `src/app/features`: funcionalidades independientes (`auth`, `pacientes`) con modelo, servicio y página.
- `src/app/layout`: navegación, usuario autenticado y contenedor visual.
- `src/app/shared`: pantallas comunes, como acceso denegado.

El feature de pacientes permite consultar, crear y eliminar registros. Los datos se obtienen desde `GET/POST/DELETE /api/paciente`; no hay datos de pacientes incluidos en la interfaz.

## Autorización por roles

Las rutas y el menú usan los roles que el servicio de autenticación firma en el claim `scope` del JWT. El registro público crea `ROLE_USER`, que puede ver pacientes; `ROLE_ADMIN` también ve Administración y puede entrar a esa ruta. Las altas públicas no conceden administración. Para habilitar el rol admin, un operador autorizado debe asociar el usuario con el rol `ROLE_ADMIN` en la base `auth_db` y volver a iniciar sesión.

El guard de autenticación verifica que haya token; el guard de rol compara los accesos con `data.roles` de cada ruta. Esta autorización de interfaz mejora la navegación; la API debe seguir validando permisos en el servidor para cada operación sensible.

Consulta el [PDF de estructura y funcionamiento](docs/estructura-frontend.pdf) para el flujo detallado.
