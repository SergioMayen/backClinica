package com.example.backDocker.controller;

import com.example.backDocker.dto.ExamenDto;
import com.example.backDocker.service.ExamenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/examen")
public class ExamenController {
    private final ExamenService service;
    public ExamenController(ExamenService service) { this.service = service; }
    @GetMapping public List<ExamenDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public ExamenDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ExamenDto registrar(@Valid @RequestBody ExamenDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public ExamenDto actualizar(@PathVariable Long id, @Valid @RequestBody ExamenDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
