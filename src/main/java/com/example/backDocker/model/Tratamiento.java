package com.example.backDocker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="tratamiento")
@Getter
@Setter
public class Tratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idCitaMedica", nullable=false)
    private CitaMedica citaMedica;
    @Column(nullable=false)
    private String descripcion;
    @Lob
    private String observaciones;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    @Column(name="usuario_creacion", nullable=false, length=100)
    private String usuarioCreacion = "SYSTEM";
    @Column(name="usuario_modificacion", length=100)
    private String usuarioModificacion;
    @Column(name="fecha_creacion", insertable=false, updatable=false)
    private LocalDateTime fechaCreacion;
    @Column(name="fecha_modificacion", insertable=false, updatable=false)
    private LocalDateTime fechaModificacion;
}
