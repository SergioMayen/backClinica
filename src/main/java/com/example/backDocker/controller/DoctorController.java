package com.example.backDocker.controller;

import com.example.backDocker.dto.DoctorDto;
import com.example.backDocker.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private final DoctorService service;
    public DoctorController(DoctorService service) { this.service = service; }
    @GetMapping public List<DoctorDto> listar() { return service.listar(); }
    @GetMapping("/{id}") public DoctorDto obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public DoctorDto registrar(@Valid @RequestBody DoctorDto dto) { return service.registrar(dto); }
    @PutMapping("/{id}") public DoctorDto actualizar(@PathVariable Long id, @Valid @RequestBody DoctorDto dto) { return service.actualizar(id, dto); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
