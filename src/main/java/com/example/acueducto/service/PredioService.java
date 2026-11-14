package com.example.acueducto.service;

import com.example.acueducto.dto.PredioRequestDTO;
import com.example.acueducto.dto.PredioResponseDTO;
import com.example.acueducto.exception.RecursoNoEncontradoException;
import com.example.acueducto.exception.ReglaDeNegocioException;
import com.example.acueducto.model.entity.Predio;
import com.example.acueducto.model.entity.Usuario;
import com.example.acueducto.repository.PredioRepository;
import com.example.acueducto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PredioService {

    private final PredioRepository predioRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias por constructor
    public PredioService(PredioRepository predioRepository, UsuarioRepository usuarioRepository) {
        this.predioRepository = predioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<PredioResponseDTO> listarTodos() {
        return predioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PredioResponseDTO obtenerPorId(Long id) {
        Predio predio = predioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Predio no encontrado con ID: " + id));
        return convertirADTO(predio);
    }

    @Transactional(readOnly = true)
    public List<PredioResponseDTO> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }
        return predioRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Transactional
    public PredioResponseDTO crear(PredioRequestDTO dto) {
        validarCamposObligatorios(dto);

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se puede crear el predio. Usuario no encontrado con ID: " + dto.usuarioId()));

        Predio nuevoPredio = new Predio(
                usuario,
                dto.sector() != null ? dto.sector().trim() : null,
                dto.via() != null ? dto.via().trim() : null,
                dto.numero() != null ? dto.numero().trim() : null,
                dto.complemento() != null ? dto.complemento().trim() : null,
                dto.tipoPredio() != null ? dto.tipoPredio().trim() : "RESIDENCIAL",
                dto.tarifaXunidad() != null ? dto.tarifaXunidad() : 0.0,
                dto.tarifa() != null ? dto.tarifa() : 0.0
        );

        Predio guardado = predioRepository.save(nuevoPredio);
        return convertirADTO(guardado);
    }

    @Transactional
    public PredioResponseDTO actualizar(Long id, PredioRequestDTO dto) {
        validarCamposObligatorios(dto);

        Predio predioExistente = predioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Predio no encontrado con ID: " + id));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + dto.usuarioId()));

        predioExistente.setUsuario(usuario);
        predioExistente.setSector(dto.sector() != null ? dto.sector().trim() : null);
        predioExistente.setVia(dto.via() != null ? dto.via().trim() : null);
        predioExistente.setNumero(dto.numero() != null ? dto.numero().trim() : null);
        predioExistente.setComplemento(dto.complemento() != null ? dto.complemento().trim() : null);
        predioExistente.setTipoPredio(dto.tipoPredio() != null ? dto.tipoPredio().trim() : "RESIDENCIAL");
        predioExistente.setTarifaXunidad(dto.tarifaXunidad());
        predioExistente.setTarifa(dto.tarifa());

        Predio actualizado = predioRepository.save(predioExistente);
        return convertirADTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!predioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Predio no encontrado con ID: " + id);
        }
        predioRepository.deleteById(id);
    }

    // Métodos auxiliares privados
    private void validarCamposObligatorios(PredioRequestDTO dto) {
        if (dto.usuarioId() == null) {
            throw new ReglaDeNegocioException("El ID del usuario propietario es obligatorio");
        }
        if (dto.tarifa() == null || dto.tarifa() < 0) {
            throw new ReglaDeNegocioException("La tarifa del predio debe ser un valor válido mayor o igual a cero");
        }
    }

    private PredioResponseDTO convertirADTO(Predio predio) {
        return new PredioResponseDTO(
                predio.getId(),
                predio.getUsuario().getId(),
                predio.getUsuario().getNombre(),
                predio.getSector(),
                predio.getVia(),
                predio.getNumero(),
                predio.getComplemento(),
                predio.getTipoPredio(),
                predio.getTarifaXunidad(),
                predio.getTarifa()
        );
    }
}