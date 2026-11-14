package com.example.acueducto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.acueducto.dto.FacturaRequestDTO;
import com.example.acueducto.dto.FacturaResponseDTO;
import com.example.acueducto.service.FacturaService;





@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    // Inyección de dependencias por constructor
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<List<FacturaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.obtenerPorId(id));
    }

    @GetMapping("/predio/{predioId}")
    public ResponseEntity<List<FacturaResponseDTO>> listarPorPredio(@PathVariable Long predioId) {
        return ResponseEntity.ok(facturaService.listarPorPredio(predioId));
    }

    @PostMapping
    public ResponseEntity<FacturaResponseDTO> crear(@RequestBody FacturaRequestDTO dto) {
        FacturaResponseDTO nuevaFactura = facturaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<FacturaResponseDTO> asentarPago(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.asentarPago(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}