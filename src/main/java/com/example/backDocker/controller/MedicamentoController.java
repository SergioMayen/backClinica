package com.example.backDocker.controller;

import com.example.backDocker.dto.MedicamentoDto;
import com.example.backDocker.service.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medicamento")
public class MedicamentoController {
    private final MedicamentoService service;
    public MedicamentoController(MedicamentoService service) { this.service = service; }
    @GetMapping public List<MedicamentoDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public MedicamentoDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public MedicamentoDto registrar(@Valid @RequestBody MedicamentoDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public MedicamentoDto actualizar(@PathVariable Long id, @Valid @RequestBody MedicamentoDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
