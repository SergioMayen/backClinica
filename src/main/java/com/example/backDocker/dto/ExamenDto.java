package com.example.backDocker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExamenDto(
        Long id,
        Long tratamientoId,
        Long tipoExamenId,
        LocalDate fechaSolicitud,
        LocalDate fechaResultado,
        String resultado,
        String observaciones,
        String estado,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
