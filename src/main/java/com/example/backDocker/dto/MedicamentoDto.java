package com.example.backDocker.dto;

import java.time.LocalDateTime;

public record MedicamentoDto(
        Long id,
        String nombre,
        String descripcion,
        String presentacion,
        String concentracion,
        Boolean activo,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
