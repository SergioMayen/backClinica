package com.example.backDocker.controller;

import com.example.backDocker.dto.DetalleRecetaDto;
import com.example.backDocker.service.DetalleRecetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/detalle_receta")
public class DetalleRecetaController {
    private final DetalleRecetaService service;
    public DetalleRecetaController(DetalleRecetaService service) { this.service = service; }
    @GetMapping public List<DetalleRecetaDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public DetalleRecetaDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public DetalleRecetaDto registrar(@Valid @RequestBody DetalleRecetaDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public DetalleRecetaDto actualizar(@PathVariable Long id, @Valid @RequestBody DetalleRecetaDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
