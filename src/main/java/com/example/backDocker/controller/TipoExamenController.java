package com.example.backDocker.controller;

import com.example.backDocker.dto.TipoExamenDto;
import com.example.backDocker.service.TipoExamenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipo_examen")
public class TipoExamenController {
    private final TipoExamenService service;
    public TipoExamenController(TipoExamenService service) { this.service = service; }
    @GetMapping public List<TipoExamenDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public TipoExamenDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public TipoExamenDto registrar(@Valid @RequestBody TipoExamenDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public TipoExamenDto actualizar(@PathVariable Long id, @Valid @RequestBody TipoExamenDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
