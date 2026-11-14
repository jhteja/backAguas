package com.example.acueducto.dto;

public record UsuarioRequestDTO(
    String documento,
    String nombre,
    String password,
    String rol
) {}