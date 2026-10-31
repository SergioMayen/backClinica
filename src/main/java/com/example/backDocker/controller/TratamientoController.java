package com.example.backDocker.controller;

import com.example.backDocker.dto.TratamientoDto;
import com.example.backDocker.service.TratamientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tratamiento")
public class TratamientoController {
    private final TratamientoService service;
    public TratamientoController(TratamientoService service) { this.service = service; }
    @GetMapping public List<TratamientoDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public TratamientoDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public TratamientoDto registrar(@Valid @RequestBody TratamientoDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public TratamientoDto actualizar(@PathVariable Long id, @Valid @RequestBody TratamientoDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
