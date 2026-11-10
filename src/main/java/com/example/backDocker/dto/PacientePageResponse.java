package com.example.backDocker.dto;

import java.util.List;

public record PacientePageResponse<T>(List<T> data, Metadata metadata) {
    public record Metadata(long totalRecords, int page, int pageSize, int totalPages,
                           boolean hasPreviousPage, boolean hasNextPage) {}
}
