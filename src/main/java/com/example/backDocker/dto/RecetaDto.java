package com.example.backDocker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RecetaDto(
        Long id,
        Long tratamientoId,
        LocalDate fechaEmision,
        String indicaciones,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
