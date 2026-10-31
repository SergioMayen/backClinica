package com.example.backDocker.service;

import com.example.backDocker.model.Examen;
import com.example.backDocker.model.Tratamiento;
import com.example.backDocker.model.TipoExamen;
import com.example.backDocker.repository.ExamenRepository;
import com.example.backDocker.repository.TratamientoRepository;
import com.example.backDocker.repository.TipoExamenRepository;
import com.example.backDocker.dto.ExamenDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ExamenService {
    private final ExamenRepository repository;
    private final TratamientoRepository tratamiento;
    private final TipoExamenRepository tipoExamen;
    public ExamenService(ExamenRepository repository, TratamientoRepository tratamiento, TipoExamenRepository tipoExamen) {
        this.repository = repository;
        this.tratamiento = tratamiento;
        this.tipoExamen = tipoExamen;
    }
    @Transactional(readOnly = true)
    public List<ExamenDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public ExamenDto obtener(Long id) { return toDto(buscar(id)); }
    public ExamenDto registrar(ExamenDto dto) { return toDto(repository.save(toEntity(dto, new Examen()))); }
    public ExamenDto actualizar(Long id, ExamenDto dto) {
        Examen entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private Examen buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Examen no encontrado: " + id)); }
    private Examen toEntity(ExamenDto dto, Examen entity) {
        entity.setTratamiento(tratamiento.getReferenceById(dto.tratamientoId()));
        entity.setTipoExamen(tipoExamen.getReferenceById(dto.tipoExamenId()));
        entity.setFechaSolicitud(dto.fechaSolicitud());
        entity.setFechaResultado(dto.fechaResultado());
        entity.setResultado(dto.resultado());
        entity.setObservaciones(dto.observaciones());
        entity.setEstado(dto.estado());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private ExamenDto toDto(Examen entity) {
        return new ExamenDto(
                entity.getId(),
                entity.getTratamiento().getId(),
                entity.getTipoExamen().getId(),
                entity.getFechaSolicitud(),
                entity.getFechaResultado(),
                entity.getResultado(),
                entity.getObservaciones(),
                entity.getEstado(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
