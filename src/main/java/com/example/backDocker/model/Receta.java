package com.example.backDocker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="receta")
@Getter
@Setter
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idTratamiento", nullable=false)
    private Tratamiento tratamiento;
    private LocalDate fechaEmision;
    @Lob
    private String indicaciones;
    @Column(name="usuario_creacion", nullable=false, length=100)
    private String usuarioCreacion = "SYSTEM";
    @Column(name="usuario_modificacion", length=100)
    private String usuarioModificacion;
    @Column(name="fecha_creacion", insertable=false, updatable=false)
    private LocalDateTime fechaCreacion;
    @Column(name="fecha_modificacion", insertable=false, updatable=false)
    private LocalDateTime fechaModificacion;
}
