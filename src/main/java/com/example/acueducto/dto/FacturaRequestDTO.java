package com.example.acueducto.dto;

import java.time.LocalDate;

public record FacturaRequestDTO(
    Long predioId,
    String periodo,
    LocalDate fechaVencimiento,
    Double valor
) {}