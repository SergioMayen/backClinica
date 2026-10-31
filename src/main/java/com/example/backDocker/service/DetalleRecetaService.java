package com.example.backDocker.service;

import com.example.backDocker.model.DetalleReceta;
import com.example.backDocker.model.Receta;
import com.example.backDocker.model.Medicamento;
import com.example.backDocker.repository.DetalleRecetaRepository;
import com.example.backDocker.repository.RecetaRepository;
import com.example.backDocker.repository.MedicamentoRepository;
import com.example.backDocker.dto.DetalleRecetaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DetalleRecetaService {
    private final DetalleRecetaRepository repository;
    private final RecetaRepository receta;
    private final MedicamentoRepository medicamento;
    public DetalleRecetaService(DetalleRecetaRepository repository, RecetaRepository receta, MedicamentoRepository medicamento) {
        this.repository = repository;
        this.receta = receta;
        this.medicamento = medicamento;
    }
    @Transactional(readOnly = true)
    public List<DetalleRecetaDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public DetalleRecetaDto obtener(Long id) { return toDto(buscar(id)); }
    public DetalleRecetaDto registrar(DetalleRecetaDto dto) { return toDto(repository.save(toEntity(dto, new DetalleReceta()))); }
    public DetalleRecetaDto actualizar(Long id, DetalleRecetaDto dto) {
        DetalleReceta entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private DetalleReceta buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("DetalleReceta no encontrado: " + id)); }
    private DetalleReceta toEntity(DetalleRecetaDto dto, DetalleReceta entity) {
        entity.setReceta(receta.getReferenceById(dto.recetaId()));
        entity.setMedicamento(medicamento.getReferenceById(dto.medicamentoId()));
        entity.setDosis(dto.dosis());
        entity.setFrecuencia(dto.frecuencia());
        entity.setDuracion(dto.duracion());
        entity.setCantidad(dto.cantidad());
        entity.setIndicaciones(dto.indicaciones());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private DetalleRecetaDto toDto(DetalleReceta entity) {
        return new DetalleRecetaDto(
                entity.getId(),
                entity.getReceta().getId(),
                entity.getMedicamento().getId(),
                entity.getDosis(),
                entity.getFrecuencia(),
                entity.getDuracion(),
                entity.getCantidad(),
                entity.getIndicaciones(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
