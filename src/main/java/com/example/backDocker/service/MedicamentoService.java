package com.example.backDocker.service;

import com.example.backDocker.model.Medicamento;
import com.example.backDocker.repository.MedicamentoRepository;
import com.example.backDocker.dto.MedicamentoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class MedicamentoService {
    private final MedicamentoRepository repository;
    public MedicamentoService(MedicamentoRepository repository) {
        this.repository = repository;
    }
    @Transactional(readOnly = true)
    public List<MedicamentoDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public MedicamentoDto obtener(Long id) { return toDto(buscar(id)); }
    public MedicamentoDto registrar(MedicamentoDto dto) { return toDto(repository.save(toEntity(dto, new Medicamento()))); }
    public MedicamentoDto actualizar(Long id, MedicamentoDto dto) {
        Medicamento entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private Medicamento buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Medicamento no encontrado: " + id)); }
    private Medicamento toEntity(MedicamentoDto dto, Medicamento entity) {
        entity.setNombre(dto.nombre());
        entity.setDescripcion(dto.descripcion());
        entity.setPresentacion(dto.presentacion());
        entity.setConcentracion(dto.concentracion());
        entity.setActivo(dto.activo());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private MedicamentoDto toDto(Medicamento entity) {
        return new MedicamentoDto(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getPresentacion(),
                entity.getConcentracion(),
                entity.getActivo(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
