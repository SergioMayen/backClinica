package com.example.backDocker.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="cita_medica")
@Getter
@Setter
public class CitaMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idPaciente", nullable=false)
    private Paciente paciente;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idDoctor", nullable=false)
    private Doctor doctor;
    private LocalDateTime fechaHora;
    private String motivo;
    private String estado = "PROGRAMADA";
    @Column(name="usuario_creacion", nullable=false, length=100)
    private String usuarioCreacion = "SYSTEM";
    @Column(name="usuario_modificacion", length=100)
    private String usuarioModificacion;
    @CreationTimestamp
    @Column(name="fecha_creacion", nullable=false)
    private LocalDateTime fechaCreacion;
    @UpdateTimestamp
    @Column(name="fecha_modificacion")
    private LocalDateTime fechaModificacion;
}
