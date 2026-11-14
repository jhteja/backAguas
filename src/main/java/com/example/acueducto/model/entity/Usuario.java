package com.example.acueducto.model.entity;

import com.example.acueducto.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario extends BaseEntity {

    @Column(name = "documento", nullable = false, unique = true, length = 20)
    private String documento;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    // Constructor vacío exigido por JPA
    public Usuario() {
    }

    // Constructor parametrizado
    public Usuario(String documento, String nombre, String password, String rol) {
        this.documento = documento;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters explícitos
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}