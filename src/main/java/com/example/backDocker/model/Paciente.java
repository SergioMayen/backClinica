package com.example.backDocker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="paciente")
@Getter
@Setter
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String nombre;
    @Column(nullable=false)
    private String apellido;
    @Column(nullable=false, unique=true, length=150)
    private String dpi;
    private String telefono;
    private String correo;
    private LocalDate fechaNacimiento;
    @Column(nullable=false)
    private Boolean activo = true;
    @Column(name="usuario_creacion", nullable=false, length=100)
    private String usuarioCreacion = "SYSTEM";
    @Column(name="usuario_modificacion", length=100)
    private String usuarioModificacion;
    @Column(name="fecha_creacion", insertable=false, updatable=false)
    private LocalDateTime fechaCreacion;
    @Column(name="fecha_modificacion", insertable=false, updatable=false)
    private LocalDateTime fechaModificacion;
}
