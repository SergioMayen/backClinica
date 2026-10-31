package com.example.backDocker.service;

import com.example.backDocker.model.Receta;
import com.example.backDocker.model.Tratamiento;
import com.example.backDocker.repository.RecetaRepository;
import com.example.backDocker.repository.TratamientoRepository;
import com.example.backDocker.dto.RecetaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RecetaService {
    private final RecetaRepository repository;
    private final TratamientoRepository tratamiento;
    public RecetaService(RecetaRepository repository, TratamientoRepository tratamiento) {
        this.repository = repository;
        this.tratamiento = tratamiento;
    }
    @Transactional(readOnly = true)
    public List<RecetaDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public RecetaDto obtener(Long id) { return toDto(buscar(id)); }
    public RecetaDto registrar(RecetaDto dto) { return toDto(repository.save(toEntity(dto, new Receta()))); }
    public RecetaDto actualizar(Long id, RecetaDto dto) {
        Receta entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private Receta buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Receta no encontrado: " + id)); }
    private Receta toEntity(RecetaDto dto, Receta entity) {
        entity.setTratamiento(tratamiento.getReferenceById(dto.tratamientoId()));
        entity.setFechaEmision(dto.fechaEmision());
        entity.setIndicaciones(dto.indicaciones());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private RecetaDto toDto(Receta entity) {
        return new RecetaDto(
                entity.getId(),
                entity.getTratamiento().getId(),
                entity.getFechaEmision(),
                entity.getIndicaciones(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
