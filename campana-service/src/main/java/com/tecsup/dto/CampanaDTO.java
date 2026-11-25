package com.tecsup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CampanaDTO {

    private Long id;

    @NotBlank(message = "El nombre de la campana es obligatorio")
    @Size(max = 120, message = "El nombre no puede pasar de 120 caracteres")
    private String nombre;

    @Size(max = 300, message = "La descripcion no puede pasar de 300 caracteres")
    private String descripcion;

    @NotNull(message = "La meta de recaudacion es obligatoria")
    @DecimalMin(value = "1.00", message = "La meta debe ser mayor que cero")
    @Digits(integer = 10, fraction = 2, message = "La meta admite como maximo dos decimales")
    private BigDecimal metaRecaudacion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @Pattern(regexp = "ACTIVA|CERRADA", message = "El estado solo puede ser ACTIVA o CERRADA")
    private String estado = "ACTIVA";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getMetaRecaudacion() { return metaRecaudacion; }
    public void setMetaRecaudacion(BigDecimal metaRecaudacion) { this.metaRecaudacion = metaRecaudacion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
