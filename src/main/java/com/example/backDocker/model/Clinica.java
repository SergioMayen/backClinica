package com.example.backDocker.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="clinica")
@Getter
@Setter
public class Clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String nombre;
    @Column(nullable=false)
    private String direccion;
    private String telefono;
    private String correo;
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
