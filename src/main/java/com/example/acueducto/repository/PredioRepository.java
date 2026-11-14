package com.example.acueducto.repository;

import com.example.acueducto.model.entity.Predio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredioRepository extends JpaRepository<Predio, Long> {

    List<Predio> findByUsuarioId(Long usuarioId);
}