package com.example.acueducto.repository;

import com.example.acueducto.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    boolean existsByFacturaId(Long facturaId);
}
