package com.example.acueducto.dto;

public record PredioRequestDTO(
    Long usuarioId,
    String sector,
    String via,
    String numero,
    String complemento,
    String tipoPredio,
    Double tarifaXunidad,
    Double tarifa
) {}