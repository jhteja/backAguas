package com.example.acueducto.dto;

public record UsuarioResponseDTO(
    Long id,
    String documento,
    String nombre,
    String rol
) {}