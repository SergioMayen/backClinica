# Estructura del frontend

```text
src/app/
├── core/
│   ├── auth/auth.service.ts
│   ├── guards/auth.guard.ts
│   └── interceptors/auth.interceptor.ts
├── features/
│   ├── auth/login.component.ts
│   └── pacientes/
│       ├── paciente.model.ts
│       ├── paciente.service.ts
│       └── pacientes.component.ts
├── layout/shell.component.ts
├── shared/denied.component.ts
├── app.routes.ts
└── app.component.ts
```

Models describen DTOs del backend; Services concentran llamadas HTTP; Pages (componentes de feature) presentan datos y capturan acciones del usuario. El interceptor agrega `Authorization: Bearer <JWT>`. La autenticación usa `POST /api/auth/login`, y el proxy la dirige al puerto 8081. La API clínica en puerto 8080 recibe las peticiones `/api/*`.

## Búsqueda y paginación desde la base de datos

```text
Angular
  -> GET + filtros + page + size
  -> PacienteController
  -> PacienteFilter (@ModelAttribute)
  -> PacienteService
  -> PacienteSpecification (Specification<Paciente>)
  -> PageRequest (Pageable + Sort id DESC)
  -> PacienteRepository (JpaSpecificationExecutor)
  -> Spring Data JPA / base de datos
  -> Page<Paciente>
  -> Page<PacienteDto> y envoltura data + metadata
  -> Angular
```

`PacienteFilter` reúne nombre, apellido, DPI, estado, página y tamaño. `PacienteSpecification` construye predicados opcionales con `Specification<Paciente>`; el texto usa coincidencia parcial sin distinguir mayúsculas y los criterios se unen con AND. El repositorio hereda `JpaSpecificationExecutor<Paciente>`, que permite ejecutar especificación y paginación en la base de datos. El servicio construye `PageRequest.of(page, size, Sort.by(DESC, "id"))`; únicamente acepta tamaños 50, 100 o 200. Ordenar antes de paginar conserva un orden estable entre peticiones consecutivas.

`Page<PacienteDto>` contiene los elementos de la página (`getContent`) junto con total de registros, número, tamaño, total de páginas y `hasPrevious/hasNext`. El controlador devuelve los DTO dentro de `data` y esa metadata en `metadata`. `totalPages` equivale al total filtrado dividido entre el tamaño de página, redondeado hacia arriba; Spring Data obtiene esos valores, incluido el conteo, aplicando la misma Specification.

Una `List` solo representa elementos, sin metadata de página. `Page<T>` representa un fragmento solicitado y su metadata. Con 50,000 filas y `size=50`, la consulta obtiene como máximo 50 entidades para esa petición, además del conteo; no carga las 50,000 para luego filtrarlas con streams. `Specification` convierte los filtros en predicados WHERE para la base de datos. `Pageable` expresa página, tamaño y orden; `PageRequest` es su implementación concreta habitual. El límite de tamaño evita que una solicitud eleve el uso de memoria y red como si fuera una descarga completa.

Angular envía los filtros y la paginación juntos a `/api/pacientes`. Buscar o cambiar tamaño vuelve a `page=0`. Anterior y Siguiente realizan otra llamada HTTP y se habilitan según `hasPreviousPage`/`hasNextPage`. Se muestra `totalRecords`, página actual y `totalPages`. Para la evidencia: abre DevTools → Network y verifica `page=0`, `page=1`, `page=2` con `size=50`; luego combina nombre y estado activo. El JSON permite comprobar elementos y metadata. También se incluye la colección Postman `postman/Pacientes-Paginacion.postman_collection.json`, que contiene las cuatro peticiones demostrativas y requiere un Bearer JWT. Ejemplos completos en `API.md`.

El menú y el guard consultan roles del claim `scope`; `/pacientes` permite `ROLE_USER` o `ROLE_ADMIN`, mientras que `/administracion` requiere `ROLE_ADMIN`. Los registros nuevos reciben `ROLE_USER` según la implementación actual del backend. La matriz comparativa de roles es una vista explicativa: se requiere iniciar sesión con dos cuentas realmente provisionadas para obtener las capturas de ejecución con datos y permisos del entorno.
