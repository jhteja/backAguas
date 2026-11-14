    package com.example.acueducto.service;

import com.example.acueducto.dto.FacturaRequestDTO;
import com.example.acueducto.dto.FacturaResponseDTO;
import com.example.acueducto.exception.RecursoNoEncontradoException;
import com.example.acueducto.exception.ReglaDeNegocioException;
import com.example.acueducto.model.entity.Factura;
import com.example.acueducto.model.entity.Predio;
import com.example.acueducto.model.enums.EstadoFactura;
import com.example.acueducto.repository.FacturaRepository;
import com.example.acueducto.repository.PredioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final PredioRepository predioRepository;

    // Inyección de dependencias por constructor
    public FacturaService(FacturaRepository facturaRepository, PredioRepository predioRepository) {
        this.facturaRepository = facturaRepository;
        this.predioRepository = predioRepository;
    }

    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> listarTodas() {
        return facturaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public FacturaResponseDTO obtenerPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada con ID: " + id));
        return convertirADTO(factura);
    }

    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> listarPorPredio(Long predioId) {
        if (!predioRepository.existsById(predioId)) {
            throw new RecursoNoEncontradoException("Predio no encontrado con ID: " + predioId);
        }
        return facturaRepository.findByPredioId(predioId)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Transactional
    public FacturaResponseDTO crear(FacturaRequestDTO dto) {
        validarCamposObligatorios(dto);

        Predio predio = predioRepository.findById(dto.predioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se puede generar la factura. Predio no encontrado con ID: " + dto.predioId()));

        // Regla de negocio: Evitar duplicidad de factura en un mismo periodo para un predio
        if (facturaRepository.existsByPredioIdAndPeriodo(dto.predioId(), dto.periodo().trim())) {
            throw new ReglaDeNegocioException("Ya existe una factura emitida para el predio ID " + dto.predioId() + " en el periodo: " + dto.periodo());
        }

        Factura nuevaFactura = new Factura(
                predio,
                dto.periodo().trim(),
                dto.fechaVencimiento(),
                dto.valor(),
                EstadoFactura.PENDIENTE,
                null // fechaPago inicia nula hasta que se asiente el pago
        );

        Factura guardada = facturaRepository.save(nuevaFactura);
        return convertirADTO(guardada);
    }

    @Transactional
    public FacturaResponseDTO asentarPago(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada con ID: " + id));

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new ReglaDeNegocioException("La factura ID " + id + " ya se encuentra PAGADA");
        }

        if (factura.getEstado() == EstadoFactura.CANCELADA) {
            throw new ReglaDeNegocioException("No se puede asentar el pago de una factura en estado CANCELADA");
        }

        // Asentamiento del pago
        factura.setEstado(EstadoFactura.PAGADA);
        factura.setFechaPago(LocalDateTime.now());

        Factura actualizada = facturaRepository.save(factura);
        return convertirADTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada con ID: " + id));

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new ReglaDeNegocioException("No se puede eliminar la factura ID " + id + " porque ya posee un pago asentado");
        }

        facturaRepository.delete(factura);
    }

    // Métodos auxiliares
    private void validarCamposObligatorios(FacturaRequestDTO dto) {
        if (dto.predioId() == null) {
            throw new ReglaDeNegocioException("El ID del predio es obligatorio");
        }
        if (dto.periodo() == null || dto.periodo().isBlank()) {
            throw new ReglaDeNegocioException("El periodo de facturación es obligatorio (ej. 2026-03)");
        }
        if (dto.fechaVencimiento() == null) {
            throw new ReglaDeNegocioException("La fecha de vencimiento es obligatoria");
        }
        if (dto.valor() == null || dto.valor() <= 0) {
            throw new ReglaDeNegocioException("El valor de la factura debe ser mayor a cero");
        }
    }

    private FacturaResponseDTO convertirADTO(Factura factura) {
        return new FacturaResponseDTO(
                factura.getId(),
                factura.getPredio().getId(),
                factura.getPeriodo(),
                factura.getFechaVencimiento(),
                factura.getValor(),
                factura.getEstado(),
                factura.getFechaPago()
        );
    }
}