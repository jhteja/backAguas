package com.example.acueducto.dto;

import com.example.acueducto.model.enums.EstadoFactura;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FacturaResponseDTO(
    Long id,
    Long predioId,
    String periodo,
    LocalDate fechaVencimiento,
    Double valor,
    EstadoFactura estado,
    LocalDateTime fechaPago
) {}