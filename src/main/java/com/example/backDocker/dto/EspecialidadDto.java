package com.example.backDocker.dto;

import java.time.LocalDateTime;

public record EspecialidadDto(
        Long id,
        String nombre,
        String descripcion,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
