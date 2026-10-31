package com.example.backDocker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="tipo_examen")
@Getter
@Setter
public class TipoExamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=150)
    private String nombre;
    @Column(nullable=false)
    private String descripcion;
    @Column(name="usuario_creacion", nullable=false, length=100)
    private String usuarioCreacion = "SYSTEM";
    @Column(name="usuario_modificacion", length=100)
    private String usuarioModificacion;
    @Column(name="fecha_creacion", insertable=false, updatable=false)
    private LocalDateTime fechaCreacion;
    @Column(name="fecha_modificacion", insertable=false, updatable=false)
    private LocalDateTime fechaModificacion;
}
