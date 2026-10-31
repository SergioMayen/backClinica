package com.example.backDocker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteDto(
        Long id,
        String nombre,
        String apellido,
        String dpi,
        String telefono,
        String correo,
        LocalDate fechaNacimiento,
        Boolean activo,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
