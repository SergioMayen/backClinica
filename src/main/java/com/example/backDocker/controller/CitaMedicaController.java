package com.example.backDocker.controller;

import com.example.backDocker.dto.CitaMedicaDto;
import com.example.backDocker.service.CitaMedicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cita_medica")
public class CitaMedicaController {
    private final CitaMedicaService service;
    public CitaMedicaController(CitaMedicaService service) { this.service = service; }
    @GetMapping public List<CitaMedicaDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public CitaMedicaDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public CitaMedicaDto registrar(@Valid @RequestBody CitaMedicaDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public CitaMedicaDto actualizar(@PathVariable Long id, @Valid @RequestBody CitaMedicaDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
