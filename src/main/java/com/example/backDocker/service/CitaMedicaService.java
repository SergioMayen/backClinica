package com.example.backDocker.service;

import com.example.backDocker.model.CitaMedica;
import com.example.backDocker.model.Paciente;
import com.example.backDocker.model.Doctor;
import com.example.backDocker.repository.CitaMedicaRepository;
import com.example.backDocker.repository.PacienteRepository;
import com.example.backDocker.repository.DoctorRepository;
import com.example.backDocker.dto.CitaMedicaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CitaMedicaService {
    private final CitaMedicaRepository repository;
    private final PacienteRepository paciente;
    private final DoctorRepository doctor;
    public CitaMedicaService(CitaMedicaRepository repository, PacienteRepository paciente, DoctorRepository doctor) {
        this.repository = repository;
        this.paciente = paciente;
        this.doctor = doctor;
    }
    @Transactional(readOnly = true)
    public List<CitaMedicaDto> listar() { return repository.findAll().stream().map(this::toDto).toList(); }
    @Transactional(readOnly = true)
    public CitaMedicaDto obtener(Long id) { return toDto(buscar(id)); }
    public CitaMedicaDto registrar(CitaMedicaDto dto) { return toDto(repository.save(toEntity(dto, new CitaMedica()))); }
    public CitaMedicaDto actualizar(Long id, CitaMedicaDto dto) {
        CitaMedica entity = buscar(id);
        return toDto(repository.save(toEntity(dto, entity)));
    }
    public void eliminar(Long id) { repository.delete(buscar(id)); }
    private CitaMedica buscar(Long id) { return repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("CitaMedica no encontrado: " + id)); }
    private CitaMedica toEntity(CitaMedicaDto dto, CitaMedica entity) {
        entity.setPaciente(paciente.getReferenceById(dto.pacienteId()));
        entity.setDoctor(doctor.getReferenceById(dto.doctorId()));
        entity.setFechaHora(dto.fechaHora());
        entity.setMotivo(dto.motivo());
        entity.setEstado(dto.estado());
        entity.setUsuarioCreacion(dto.usuarioCreacion() == null ? "SYSTEM" : dto.usuarioCreacion());
        entity.setUsuarioModificacion(dto.usuarioModificacion());
        return entity;
    }
    private CitaMedicaDto toDto(CitaMedica entity) {
        return new CitaMedicaDto(
                entity.getId(),
                entity.getPaciente().getId(),
                entity.getDoctor().getId(),
                entity.getFechaHora(),
                entity.getMotivo(),
                entity.getEstado(),
                entity.getUsuarioCreacion(),
                entity.getUsuarioModificacion(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }
}
