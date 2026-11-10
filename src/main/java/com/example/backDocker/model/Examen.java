package com.example.backDocker.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Entity
@Table(name="examen")
@Getter
@Setter
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idTratamiento", nullable=false)
    private Tratamiento tratamiento;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="idTipoExamen", nullable=false)
    private TipoExamen tipoExamen;
    private LocalDate fechaSolicitud;
    private LocalDate fechaResultado;
    @Lob
    private String resultado;
    @Lob
    private String observaciones;
    private String estado = "PENDIENTE";
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
