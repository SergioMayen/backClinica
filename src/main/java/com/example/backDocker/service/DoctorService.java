package com.example.backDocker.service;

import com.example.backDocker.model.Doctor;
import com.example.backDocker.model.Especialidad;
import com.example.backDocker.model.Clinica;
import com.example.backDocker.repository.DoctorRepository;
import com.example.backDocker.repository.EspecialidadRepository;
import com.example.backDocker.repository.ClinicaRepository;
import com.example.backDocker.dto.DoctorDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DoctorService {
    private final DoctorRepository repository;
    private final EspecialidadRepository especialidad;
    private final ClinicaRepository clinica;
    public DoctorService(DoctorRepository repository, EspecialidadRepository especialidad, ClinicaRepository clinica) {
        this.repository = repository;
        this.especialidad = especialidad;
        this.clinica = clinica;
    }
    @Transactional(readOnly = true)
    public List<DoctorDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public DoctorDto obtener(Long id) { return toDto(buscar(id)); }
    public DoctorDto registrar(DoctorDto dto) { return toDto(repository.save(toEntity(dto, new Doctor()))); }
    public DoctorDto actualizar(Long id, DoctorDto dto) {
        Doctor entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private Doctor buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Doctor no encontrado: " + id)); }
    private Doctor toEntity(DoctorDto dto, Doctor entity) {
        entity.setNombre(dto.nombre());
        entity.setApellido(dto.apellido());
        entity.setColegiado(dto.colegiado());
        entity.setTelefono(dto.telefono());
        entity.setCorreo(dto.correo());
        entity.setEspecialidad(especialidad.getReferenceById(dto.especialidadId()));
        entity.setClinica(clinica.getReferenceById(dto.clinicaId()));
        entity.setActivo(dto.activo());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private DoctorDto toDto(Doctor entity) {
        return new DoctorDto(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getColegiado(),
                entity.getTelefono(),
                entity.getCorreo(),
                entity.getEspecialidad().getId(),
                entity.getClinica().getId(),
                entity.getActivo(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
