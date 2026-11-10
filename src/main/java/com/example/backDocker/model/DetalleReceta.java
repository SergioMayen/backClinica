package com.example.backDocker.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="detalle_receta")
@Getter
@Setter
public class DetalleReceta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idReceta", nullable=false)
    private Receta receta;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idMedicamento", nullable=false)
    private Medicamento medicamento;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private Integer cantidad = 1;
    @Lob
    private String indicaciones;
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
