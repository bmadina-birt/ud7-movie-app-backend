package eus.birt.dam.controller;

import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.domain.Usuario;
import eus.birt.dam.domain.Valoracion;
import eus.birt.dam.dto.ValoracionDTO;
import eus.birt.dam.dto.ValoracionMediaDTO;
import eus.birt.dam.service.PeliculaService;
import eus.birt.dam.service.UsuarioService;
import eus.birt.dam.service.ValoracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/peliculas")
@CrossOrigin(origins = "*")
public class ValoracionController {

    @Autowired
    private ValoracionService valoracionService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PeliculaService peliculaService;

    /**
     * 1. Obtener valoraciones de una película
     */
    @GetMapping("/{peliculaId}/ratings")
    public ResponseEntity<List<ValoracionDTO>> obtenerValoracionesPorPelicula(@PathVariable Long peliculaId) {
        List<ValoracionDTO> valoraciones = valoracionService.obtenerValoracionesPorPelicula(peliculaId)
            .stream()
            .map(this::convertirADTO)
            .toList();
        return ResponseEntity.ok(valoraciones);
    }
    
    /**
     * 2. Obtener valoración media de una película
     */
    @GetMapping("/{peliculaId}/ratings/average")
    public ResponseEntity<ValoracionMediaDTO> obtenerValoracionMedia(@PathVariable Long peliculaId) {
        Double media = valoracionService.calcularValoracionMedia(peliculaId);
        return ResponseEntity.ok(new ValoracionMediaDTO(media));
    }
    
    /**
     * 3. Obtener valoración de un usuario específico (usuario actual)
     */
    @GetMapping("/{peliculaId}/ratings/user")
    public ResponseEntity<ValoracionDTO> obtenerValoracionUsuarioActual(@PathVariable Long peliculaId) {
        Usuario usuario = usuarioService.obtenerUsuarioActual();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Optional<Valoracion> valoracion = valoracionService.obtenerValoracionPorUsuarioYPelicula(usuario.getId(), peliculaId);
        return valoracion
            .map(v -> ResponseEntity.ok(convertirADTO(v)))
            .orElse(ResponseEntity.noContent().build());
    }
    
    /**
     * 4. Guardar nueva valoración - Modificado para ignorar userId y usar usuario autenticado
     */
    @PostMapping("/{peliculaId}/ratings")
    public ResponseEntity<ValoracionDTO> guardarValoracion(
            @PathVariable Long peliculaId,
            @RequestBody ValoracionDTO valoracionDTO) {
        
        // Siempre obtenemos el usuario del contexto de seguridad, nunca del DTO
        Usuario usuario = usuarioService.obtenerUsuarioActual();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Optional<Pelicula> pelicula = peliculaService.obtenerPorId(peliculaId);
        if (pelicula.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Verificar si ya existe una valoración para actualizar
        Optional<Valoracion> valoracionExistente = 
            valoracionService.obtenerValoracionPorUsuarioYPelicula(usuario.getId(), peliculaId);
        
        if (valoracionExistente.isPresent()) {
            // Si ya existe, actualizar
            Valoracion v = valoracionExistente.get();
            v.setPuntuacion(valoracionDTO.getRating());
            v.setComentario(valoracionDTO.getComentario());
            v.setFecha(LocalDateTime.now());
            
            Valoracion actualizada = valoracionService.guardarValoracion(v);
            return ResponseEntity.ok(convertirADTO(actualizada));
        } else {
            // Crear nueva valoración
            Valoracion nuevaValoracion = new Valoracion();
            nuevaValoracion.setUsuario(usuario);
            nuevaValoracion.setPelicula(pelicula.get());
            nuevaValoracion.setPuntuacion(valoracionDTO.getRating());
            nuevaValoracion.setComentario(valoracionDTO.getComentario());
            nuevaValoracion.setFecha(LocalDateTime.now());
            
            Valoracion guardada = valoracionService.guardarValoracion(nuevaValoracion);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(guardada));
        }
    }
    
    /**
     * 5. Actualizar valoración existente
     */
    @PutMapping("/{peliculaId}/ratings")
    public ResponseEntity<ValoracionDTO> actualizarValoracion(
            @PathVariable Long peliculaId,
            @RequestBody ValoracionDTO valoracionDTO) {
        
        Usuario usuario = usuarioService.obtenerUsuarioActual();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Obtener la valoración existente
        Optional<Valoracion> valoracionExistente = 
            valoracionService.obtenerValoracionPorUsuarioYPelicula(usuario.getId(), peliculaId);
        
        if (valoracionExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Actualizar valoración
        Valoracion v = valoracionExistente.get();
        v.setPuntuacion(valoracionDTO.getRating());
        v.setComentario(valoracionDTO.getComentario());
        v.setFecha(LocalDateTime.now());
        
        Valoracion actualizada = valoracionService.guardarValoracion(v);
        return ResponseEntity.ok(convertirADTO(actualizada));
    }
    
    /**
     * 6. Eliminar valoración
     */
    @DeleteMapping("/{peliculaId}/ratings")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable Long peliculaId) {
        Usuario usuario = usuarioService.obtenerUsuarioActual();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        boolean eliminado = valoracionService.eliminarValoracionPorUsuarioYPelicula(usuario.getId(), peliculaId);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Método auxiliar para convertir Valoracion a ValoracionDTO
     */
    private ValoracionDTO convertirADTO(Valoracion valoracion) {
        ValoracionDTO dto = new ValoracionDTO();
        dto.setId(valoracion.getId());
        dto.setUserId(valoracion.getUsuario().getId());
        dto.setPeliculaId(valoracion.getPelicula().getId());
        dto.setRating(valoracion.getPuntuacion());
        dto.setComentario(valoracion.getComentario());
        dto.setFecha(valoracion.getFecha());
        dto.setNombreUsuario(valoracion.getUsuario().getNombreUsuario());
        return dto;
    }
}