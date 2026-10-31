package com.example.backDocker.controller;

import com.example.backDocker.dto.PacienteDto;
import com.example.backDocker.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/paciente")
public class PacienteController {
    private final PacienteService service;
    public PacienteController(PacienteService service) { this.service = service; }
    @GetMapping public List<PacienteDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public PacienteDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public PacienteDto registrar(@Valid @RequestBody PacienteDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public PacienteDto actualizar(@PathVariable Long id, @Valid @RequestBody PacienteDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
