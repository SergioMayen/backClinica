package com.example.backDocker.service;

import com.example.backDocker.model.TipoExamen;
import com.example.backDocker.repository.TipoExamenRepository;
import com.example.backDocker.dto.TipoExamenDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TipoExamenService {
    private final TipoExamenRepository repository;
    public TipoExamenService(TipoExamenRepository repository) {
        this.repository = repository;
    }
    @Transactional(readOnly = true)
    public List<TipoExamenDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public TipoExamenDto obtener(Long id) { return toDto(buscar(id)); }
    public TipoExamenDto registrar(TipoExamenDto dto) { return toDto(repository.save(toEntity(dto, new TipoExamen()))); }
    public TipoExamenDto actualizar(Long id, TipoExamenDto dto) {
        TipoExamen entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private TipoExamen buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("TipoExamen no encontrado: " + id)); }
    private TipoExamen toEntity(TipoExamenDto dto, TipoExamen entity) {
        entity.setNombre(dto.nombre());
        entity.setDescripcion(dto.descripcion());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private TipoExamenDto toDto(TipoExamen entity) {
        return new TipoExamenDto(
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
