package com.example.backDocker.controller;

import com.example.backDocker.dto.ClinicaDto;
import com.example.backDocker.service.ClinicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clinica")
public class ClinicaController {
    private final ClinicaService service;
    public ClinicaController(ClinicaService service) { this.service = service; }
    @GetMapping public List<ClinicaDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public ClinicaDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ClinicaDto registrar(@Valid @RequestBody ClinicaDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public ClinicaDto actualizar(@PathVariable Long id, @Valid @RequestBody ClinicaDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
