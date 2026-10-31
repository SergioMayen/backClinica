package com.example.backDocker.service;

import com.example.backDocker.model.Clinica;
import com.example.backDocker.repository.ClinicaRepository;
import com.example.backDocker.dto.ClinicaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ClinicaService {
    private final ClinicaRepository repository;
    public ClinicaService(ClinicaRepository repository) {
        this.repository = repository;
    }
    @Transactional(readOnly = true)
    public List<ClinicaDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public ClinicaDto obtener(Long id) { return toDto(buscar(id)); }
    public ClinicaDto registrar(ClinicaDto dto) { return toDto(repository.save(toEntity(dto, new Clinica()))); }
    public ClinicaDto actualizar(Long id, ClinicaDto dto) {
        Clinica entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private Clinica buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Clinica no encontrado: " + id)); }
    private Clinica toEntity(ClinicaDto dto, Clinica entity) {
        entity.setNombre(dto.nombre());
        entity.setDireccion(dto.direccion());
        entity.setTelefono(dto.telefono());
        entity.setCorreo(dto.correo());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private ClinicaDto toDto(Clinica entity) {
        return new ClinicaDto(
                entity.getId(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getCorreo(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
