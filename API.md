# Endpoints CRUD de la API clínica

Base URL local: `http://localhost:8080`.

Cada módulo implementa los cinco endpoints listados en la tabla:

| Módulo | Listar | Obtener por ID | Crear | Actualizar | Eliminar |
|---|---|---|---|---|---|
| Especialidades | `GET /api/especialidad` | `GET /api/especialidad/{id}` | `POST /api/especialidad` | `PUT /api/especialidad/{id}` | `DELETE /api/especialidad/{id}` |
| Clínicas | `GET /api/clinica` | `GET /api/clinica/{id}` | `POST /api/clinica` | `PUT /api/clinica/{id}` | `DELETE /api/clinica/{id}` |
| Pacientes | `GET /api/paciente` | `GET /api/paciente/{id}` | `POST /api/paciente` | `PUT /api/paciente/{id}` | `DELETE /api/paciente/{id}` |
| Doctores | `GET /api/doctor` | `GET /api/doctor/{id}` | `POST /api/doctor` | `PUT /api/doctor/{id}` | `DELETE /api/doctor/{id}` |
| Citas médicas | `GET /api/cita_medica` | `GET /api/cita_medica/{id}` | `POST /api/cita_medica` | `PUT /api/cita_medica/{id}` | `DELETE /api/cita_medica/{id}` |
| Tratamientos | `GET /api/tratamiento` | `GET /api/tratamiento/{id}` | `POST /api/tratamiento` | `PUT /api/tratamiento/{id}` | `DELETE /api/tratamiento/{id}` |
| Tipos de examen | `GET /api/tipo_examen` | `GET /api/tipo_examen/{id}` | `POST /api/tipo_examen` | `PUT /api/tipo_examen/{id}` | `DELETE /api/tipo_examen/{id}` |
| Exámenes | `GET /api/examen` | `GET /api/examen/{id}` | `POST /api/examen` | `PUT /api/examen/{id}` | `DELETE /api/examen/{id}` |
| Recetas | `GET /api/receta` | `GET /api/receta/{id}` | `POST /api/receta` | `PUT /api/receta/{id}` | `DELETE /api/receta/{id}` |
| Medicamentos | `GET /api/medicamento` | `GET /api/medicamento/{id}` | `POST /api/medicamento` | `PUT /api/medicamento/{id}` | `DELETE /api/medicamento/{id}` |
| Detalles de receta | `GET /api/detalle_receta` | `GET /api/detalle_receta/{id}` | `POST /api/detalle_receta` | `PUT /api/detalle_receta/{id}` | `DELETE /api/detalle_receta/{id}` |

`POST` responde `201 Created`; `DELETE` responde `204 No Content`. Los endpoints reciben y devuelven DTOs. Para relaciones, se envía el identificador como campo terminado en `Id`. Ejemplo para crear un doctor:

```json
{
  "nombre": "Ana",
  "apellido": "López",
  "colegiado": "MED-123",
  "especialidadId": 1,
  "clinicaId": 1,
  "activo": true
}
```

Para crear una cita médica, por ejemplo, usa `POST /api/cita_medica` con un cuerpo como:

```json
{
  "pacienteId": 1,
  "doctorId": 1,
  "fechaHora": "2026-11-01T10:30:00",
  "motivo": "Consulta general",
  "estado": "PROGRAMADA"
}
```
