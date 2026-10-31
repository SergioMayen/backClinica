package com.example.backDocker.controller;

import com.example.backDocker.dto.RecetaDto;
import com.example.backDocker.service.RecetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/receta")
public class RecetaController {
    private final RecetaService service;
    public RecetaController(RecetaService service) { this.service = service; }
    @GetMapping public List<RecetaDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public RecetaDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public RecetaDto registrar(@Valid @RequestBody RecetaDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public RecetaDto actualizar(@PathVariable Long id, @Valid @RequestBody RecetaDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
