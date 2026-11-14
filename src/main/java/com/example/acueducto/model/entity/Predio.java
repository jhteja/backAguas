package com.example.acueducto.model.entity;

import com.example.acueducto.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "predios")
public class Predio extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "sector", length = 50)
    private String sector;

    @Column(name = "via", length = 50)
    private String via;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "tipo_predio", length = 50)
    private String tipoPredio;

    @Column(name = "tarifa_x_unidad")
    private Double tarifaXunidad;

    @Column(name = "tarifa")
    private Double tarifa;

    // Constructor sin parámetros exigido por JPA
    public Predio() {
    }

    // Constructor parametrizado
    public Predio(Usuario usuario, String sector, String via, String numero, String complemento, String tipoPredio, Double tarifaXunidad, Double tarifa) {
        this.usuario = usuario;
        this.sector = sector;
        this.via = via;
        this.numero = numero;
        this.complemento = complemento;
        this.tipoPredio = tipoPredio;
        this.tarifaXunidad = tarifaXunidad;
        this.tarifa = tarifa;
    }

    // Getters y Setters explícitos (Java estándar)
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public Double getTarifaXunidad() {
        return tarifaXunidad;
    }

    public void setTarifaXunidad(Double tarifaXunidad) {
        this.tarifaXunidad = tarifaXunidad;
    }

    public Double getTarifa() {
        return tarifa;
    }

    public void setTarifa(Double tarifa) {
        this.tarifa = tarifa;
    }
}