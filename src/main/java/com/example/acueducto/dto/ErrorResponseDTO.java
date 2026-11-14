package com.example.acueducto.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
    int status,
    String error,
    String message,
    String path,
    LocalDateTime timestamp
) {}