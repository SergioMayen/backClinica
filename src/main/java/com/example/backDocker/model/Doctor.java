package com.example.backDocker.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="doctor")
@Getter
@Setter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String nombre;
    @Column(nullable=false)
    private String apellido;
    @Column(nullable=false, unique=true, length=150)
    private String colegiado;
    private String telefono;
    private String correo;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idEspecialidad", nullable=false)
    private Especialidad especialidad;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idClinica", nullable=false)
    private Clinica clinica;
    @Column(nullable=false)
    private Boolean activo = true;
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
