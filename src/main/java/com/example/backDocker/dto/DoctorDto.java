package com.example.backDocker.dto;

import java.time.LocalDateTime;

public record DoctorDto(
        Long id,
        String nombre,
        String apellido,
        String colegiado,
        String telefono,
        String correo,
        Long especialidadId,
        Long clinicaId,
        Boolean activo,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
