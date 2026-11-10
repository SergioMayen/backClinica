package com.example.backDocker.controller;

import com.example.backDocker.dto.PacienteDto;
import com.example.backDocker.dto.PacienteFilter;
import com.example.backDocker.dto.PacientePageResponse;
import com.example.backDocker.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/paciente", "/api/pacientes"})
public class PacienteController {
    private final PacienteService service;
    public PacienteController(PacienteService service) { this.service = service; }
    @GetMapping public PacientePageResponse<PacienteDto> listar(@ModelAttribute PacienteFilter filter) {
        Page<PacienteDto> result = service.listar(filter);
        PacientePageResponse.Metadata metadata = new PacientePageResponse.Metadata(
                result.getTotalElements(), result.getNumber(), result.getSize(), result.getTotalPages(),
                result.hasPrevious(), result.hasNext());
        return new PacientePageResponse<>(result.getContent(), metadata);
    }
    @GetMapping("/{id}") public PacienteDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public PacienteDto registrar(@Valid @RequestBody PacienteDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public PacienteDto actualizar(@PathVariable Long id, @Valid @RequestBody PacienteDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
