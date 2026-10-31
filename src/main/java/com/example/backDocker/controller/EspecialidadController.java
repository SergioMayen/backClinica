package com.example.backDocker.controller;

import com.example.backDocker.dto.EspecialidadDto;
import com.example.backDocker.service.EspecialidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/especialidad")
public class EspecialidadController {
    private final EspecialidadService service;
    public EspecialidadController(EspecialidadService service) { this.service = service; }
    @GetMapping public List<EspecialidadDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public EspecialidadDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public EspecialidadDto registrar(@Valid @RequestBody EspecialidadDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public EspecialidadDto actualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
