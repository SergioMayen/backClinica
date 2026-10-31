package com.example.backDocker.dto;

import java.time.LocalDateTime;

public record ClinicaDto(
        Long id,
        String nombre,
        String direccion,
        String telefono,
        String correo,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
