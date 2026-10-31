package com.example.backDocker.service;

import com.example.backDocker.model.Especialidad;
import com.example.backDocker.repository.EspecialidadRepository;
import com.example.backDocker.dto.EspecialidadDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class EspecialidadService {
    private final EspecialidadRepository repository;
    public EspecialidadService(EspecialidadRepository repository) {
        this.repository = repository;
    }
    @Transactional(readOnly = true)
    public List<EspecialidadDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public EspecialidadDto obtener(Long id) { return toDto(buscar(id)); }
    public EspecialidadDto registrar(EspecialidadDto dto) { return toDto(repository.save(toEntity(dto, new Especialidad()))); }
    public EspecialidadDto actualizar(Long id, EspecialidadDto dto) {
        Especialidad entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private Especialidad buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Especialidad no encontrado: " + id)); }
    private Especialidad toEntity(EspecialidadDto dto, Especialidad entity) {
        entity.setNombre(dto.nombre());
        entity.setDescripcion(dto.descripcion());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private EspecialidadDto toDto(Especialidad entity) {
        return new EspecialidadDto(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
