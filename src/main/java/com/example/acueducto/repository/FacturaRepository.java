package com.example.acueducto.repository;

import com.example.acueducto.model.entity.Factura;
import com.example.acueducto.model.enums.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository; // <-- Quitamos import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> { // <-- Quitamos @Repository

    List<Factura> findByPredioId(Long predioId);

    List<Factura> findByEstado(EstadoFactura estado);

    boolean existsByPredioIdAndPeriodo(Long predioId, String periodo);

    Optional<Factura> findByPredioIdAndPeriodo(Long predioId, String periodo);
}