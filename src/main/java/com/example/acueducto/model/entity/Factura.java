package com.example.acueducto.model.entity;

import com.example.acueducto.model.base.BaseEntity;
import com.example.acueducto.model.enums.EstadoFactura;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
public class Factura extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "predio_id", nullable = false)
    private Predio predio;

    @Column(name = "periodo", nullable = false, length = 20)
    private String periodo;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoFactura estado;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    // Constructor vacío exigido por JPA
    public Factura() {
    }

    // Constructor parametrizado
    public Factura(Predio predio, String periodo, LocalDate fechaVencimiento, Double valor, EstadoFactura estado, LocalDateTime fechaPago) {
        this.predio = predio;
        this.periodo = periodo;
        this.fechaVencimiento = fechaVencimiento;
        this.valor = valor;
        this.estado = estado;
        this.fechaPago = fechaPago;
    }

    // Getters y Setters explícitos
    public Predio getPredio() {
        return predio;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }
}