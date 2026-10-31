package com.example.backDocker.dto;

import java.time.LocalDateTime;

public record CitaMedicaDto(
        Long id,
        Long pacienteId,
        Long doctorId,
        LocalDateTime fechaHora,
        String motivo,
        String estado,
        String usuarioCreacion,
        String usuarioModificacion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
