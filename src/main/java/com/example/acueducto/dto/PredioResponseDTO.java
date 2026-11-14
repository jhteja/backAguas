package com.example.acueducto.dto;

public record PredioResponseDTO(
    Long id,
    Long usuarioId,
    String nombreUsuario,
    String sector,
    String via,
    String numero,
    String complemento,
    String tipoPredio,
    Double tarifaXunidad,
    Double tarifa
) {}