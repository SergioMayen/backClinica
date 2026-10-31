package com.example.backDocker.dto;

import java.time.LocalDateTime;

public record DetalleRecetaDto(
        Long id,
        Long recetaId,
        Long medicamentoId,
        String dosis,
        String frecuencia,
        String duracion,
        Integer cantidad,
        String indicaciones,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
