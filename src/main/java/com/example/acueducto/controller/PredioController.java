package com.example.acueducto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.acueducto.dto.PredioRequestDTO;
import com.example.acueducto.dto.PredioResponseDTO;
import com.example.acueducto.service.PredioService;




@RestController
@RequestMapping("/api/predios")
public class PredioController {

    private final PredioService predioService;

    // Inyección de dependencias por constructor
    public PredioController(PredioService predioService) {
        this.predioService = predioService;
    }

    @GetMapping
    public ResponseEntity<List<PredioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(predioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PredioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(predioService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PredioResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(predioService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<PredioResponseDTO> crear(@RequestBody PredioRequestDTO dto) {
        PredioResponseDTO nuevoPredio = predioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPredio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PredioResponseDTO> actualizar(@PathVariable Long id, @RequestBody PredioRequestDTO dto) {
        return ResponseEntity.ok(predioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        predioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}