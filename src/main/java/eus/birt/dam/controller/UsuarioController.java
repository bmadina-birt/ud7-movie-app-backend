package eus.birt.dam.controller;

import eus.birt.dam.domain.Usuario;
import eus.birt.dam.dto.UsuarioDTO;
import eus.birt.dam.dto.UsuarioRegistroDTO;
import eus.birt.dam.mapper.EntityDtoMapper;
import eus.birt.dam.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Registro de usuario (POST) - Modificado para usar DTO
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO registroDTO) {
        Usuario usuario = EntityDtoMapper.toUsuario(registroDTO);
        Usuario guardado = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EntityDtoMapper.toUsuarioDTO(guardado));
    }

    // Obtener todos los usuarios (GET) - Modificado para usar DTO
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        List<UsuarioDTO> usuariosDTO = usuarioService.obtenerTodos()
                .stream()
                .map(EntityDtoMapper::toUsuarioDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }

    // Obtener usuario por ID (GET) - Modificado para usar DTO
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(usuario -> ResponseEntity.ok(EntityDtoMapper.toUsuarioDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Obtener datos del usuario actual
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> obtenerMiPerfil() {
        Usuario usuario = usuarioService.obtenerUsuarioActual();
        if (usuario != null) {
            return ResponseEntity.ok(EntityDtoMapper.toUsuarioDTO(usuario));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
