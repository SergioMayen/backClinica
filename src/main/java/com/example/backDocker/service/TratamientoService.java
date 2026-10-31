package com.example.backDocker.service;

import com.example.backDocker.model.Tratamiento;
import com.example.backDocker.model.CitaMedica;
import com.example.backDocker.repository.TratamientoRepository;
import com.example.backDocker.repository.CitaMedicaRepository;
import com.example.backDocker.dto.TratamientoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TratamientoService {
    private final TratamientoRepository repository;
    private final CitaMedicaRepository citaMedica;
    public TratamientoService(TratamientoRepository repository, CitaMedicaRepository citaMedica) {
        this.repository = repository;
        this.citaMedica = citaMedica;
    }
    @Transactional(readOnly = true)
    public List<TratamientoDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public TratamientoDto obtener(Long id) { return toDto(buscar(id)); }
    public TratamientoDto registrar(TratamientoDto dto) { return toDto(repository.save(toEntity(dto, new Tratamiento()))); }
    public TratamientoDto actualizar(Long id, TratamientoDto dto) {
        Tratamiento entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private Tratamiento buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Tratamiento no encontrado: " + id)); }
    private Tratamiento toEntity(TratamientoDto dto, Tratamiento entity) {
        entity.setCitaMedica(citaMedica.getReferenceById(dto.citaMedicaId()));
        entity.setDescripcion(dto.descripcion());
        entity.setObservaciones(dto.observaciones());
        entity.setFechaInicio(dto.fechaInicio());
        entity.setFechaFin(dto.fechaFin());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private TratamientoDto toDto(Tratamiento entity) {
        return new TratamientoDto(
                entity.getId(),
                entity.getCitaMedica().getId(),
                entity.getDescripcion(),
                entity.getObservaciones(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
