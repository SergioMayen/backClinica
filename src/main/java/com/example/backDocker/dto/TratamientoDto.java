package com.example.backDocker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TratamientoDto(
        Long id,
        Long citaMedicaId,
        String descripcion,
        String observaciones,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
