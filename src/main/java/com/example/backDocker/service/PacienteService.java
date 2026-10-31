package com.example.backDocker.service;

import com.example.backDocker.model.Paciente;
import com.example.backDocker.repository.PacienteRepository;
import com.example.backDocker.dto.PacienteDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PacienteService {
    private final PacienteRepository repository;
    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }
    @Transactional(readOnly = true)
    public List<PacienteDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public PacienteDto obtener(Long id) { return toDto(buscar(id)); }
    public PacienteDto registrar(PacienteDto dto) { return toDto(repository.save(toEntity(dto, new Paciente()))); }
    public PacienteDto actualizar(Long id, PacienteDto dto) {
        Paciente entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private Paciente buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Paciente no encontrado: " + id)); }
    private Paciente toEntity(PacienteDto dto, Paciente entity) {
        entity.setNombre(dto.nombre());
        entity.setApellido(dto.apellido());
        entity.setDpi(dto.dpi());
        entity.setTelefono(dto.telefono());
        entity.setCorreo(dto.correo());
        entity.setFechaNacimiento(dto.fechaNacimiento());
        entity.setActivo(dto.activo());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private PacienteDto toDto(Paciente entity) {
        return new PacienteDto(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getDpi(),
                entity.getTelefono(),
                entity.getCorreo(),
                entity.getFechaNacimiento(),
                entity.getActivo(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
