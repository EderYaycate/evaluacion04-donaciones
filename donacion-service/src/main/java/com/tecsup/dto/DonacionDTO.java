package com.tecsup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DonacionDTO {

    private Long id;

    @NotNull(message = "El id del donante es obligatorio")
    @Positive(message = "El id del donante debe ser positivo")
    private Long donanteId;

    @NotNull(message = "El id de la campana es obligatorio")
    @Positive(message = "El id de la campana debe ser positivo")
    private Long campanaId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "1.00", message = "El monto minimo de una donacion es 1.00")
    @Digits(integer = 10, fraction = 2, message = "El monto admite como maximo dos decimales")
    private BigDecimal monto;

    @NotNull(message = "La fecha de la donacion es obligatoria")
    @PastOrPresent(message = "La fecha no puede estar en el futuro")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaDonacion;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Pattern(regexp = "EFECTIVO|TARJETA|TRANSFERENCIA|YAPE",
             message = "El metodo de pago debe ser EFECTIVO, TARJETA, TRANSFERENCIA o YAPE")
    private String metodoPago;

    // Datos que llegan de los otros microservicios, no se guardan aqui
    private DonanteDTO donante;
    private CampanaDTO campana;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDonanteId() { return donanteId; }
    public void setDonanteId(Long donanteId) { this.donanteId = donanteId; }

    public Long getCampanaId() { return campanaId; }
    public void setCampanaId(Long campanaId) { this.campanaId = campanaId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFechaDonacion() { return fechaDonacion; }
    public void setFechaDonacion(LocalDate fechaDonacion) { this.fechaDonacion = fechaDonacion; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public DonanteDTO getDonante() { return donante; }
    public void setDonante(DonanteDTO donante) { this.donante = donante; }

    public CampanaDTO getCampana() { return campana; }
    public void setCampana(CampanaDTO campana) { this.campana = campana; }
}
