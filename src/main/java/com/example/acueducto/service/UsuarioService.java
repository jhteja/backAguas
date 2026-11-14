package com.example.acueducto.service;

import com.example.acueducto.dto.UsuarioRequestDTO;
import com.example.acueducto.dto.UsuarioResponseDTO;
import com.example.acueducto.exception.RecursoNoEncontradoException;
import com.example.acueducto.exception.ReglaDeNegocioException;
import com.example.acueducto.model.entity.Usuario;
import com.example.acueducto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias por constructor
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
        return convertirADTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorDocumento(String documento) {
        Usuario usuario = usuarioRepository.findByDocumento(documento)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con documento: " + documento));
        return convertirADTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        validarCamposObligatorios(dto);

        if (usuarioRepository.existsByDocumento(dto.documento().trim())) {
            throw new ReglaDeNegocioException("Ya existe un usuario registrado con el documento: " + dto.documento());
        }

        Usuario nuevoUsuario = new Usuario(
                dto.documento().trim(),
                dto.nombre().trim(),
                dto.password(),
                dto.rol() != null ? dto.rol() : "USER"
        );

        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        return convertirADTO(guardado);
    }

    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        validarCamposObligatorios(dto);

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        // Si el documento cambia, validar que no pertenezca a otro usuario
        if (!usuarioExistente.getDocumento().equals(dto.documento().trim()) &&
                usuarioRepository.existsByDocumento(dto.documento().trim())) {
            throw new ReglaDeNegocioException("El documento " + dto.documento() + " ya está asignado a otro usuario");
        }

        usuarioExistente.setDocumento(dto.documento().trim());
        usuarioExistente.setNombre(dto.nombre().trim());
        usuarioExistente.setPassword(dto.password());
        if (dto.rol() != null) {
            usuarioExistente.setRol(dto.rol());
        }

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return convertirADTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // Métodos privados auxiliares para validación y mapeo
    private void validarCamposObligatorios(UsuarioRequestDTO dto) {
        if (dto.documento() == null || dto.documento().isBlank()) {
            throw new ReglaDeNegocioException("El documento del usuario es obligatorio");
        }
        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new ReglaDeNegocioException("El nombre del usuario es obligatorio");
        }
        if (dto.password() == null || dto.password().isBlank()) {
            throw new ReglaDeNegocioException("La contraseña es obligatoria");
        }
    }

    private UsuarioResponseDTO convertirADTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getDocumento(),
                usuario.getNombre(),
                usuario.getRol()
        );
    }
}