package com.tecsup.dto;

import java.math.BigDecimal;

/** Copia local de lo que devuelve campana-service. */
public class CampanaDTO {

    private Long id;
    private String nombre;
    private BigDecimal metaRecaudacion;
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getMetaRecaudacion() { return metaRecaudacion; }
    public void setMetaRecaudacion(BigDecimal metaRecaudacion) { this.metaRecaudacion = metaRecaudacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
