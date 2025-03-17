package eus.birt.dam.controller;

import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.dto.PeliculaDTO;
import eus.birt.dam.mapper.EntityDtoMapper;
import eus.birt.dam.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtener todas las películas favoritas del usuario actual
     * Devuelve siempre 200 OK con una lista (vacía si no hay favoritos)
     */
    @GetMapping
    public ResponseEntity<List<PeliculaDTO>> obtenerFavoritos() {
        List<Pelicula> favoritos = usuarioService.obtenerFavoritosUsuarioActual();
        
        // Convertir a DTOs
        List<PeliculaDTO> favoritosDTOs = favoritos.stream()
            .map(p -> {
                PeliculaDTO dto = EntityDtoMapper.toPeliculaDTO(p);
                dto.setEsFavorito(true); // Sabemos que es favorito
                return dto;
            })
            .collect(Collectors.toList());
        
        // Siempre devolvemos 200 OK, incluso con lista vacía
        return ResponseEntity.ok(favoritosDTOs);
    }

    /**
     * Verificar si una película está en favoritos
     * Devuelve 200 OK si es favorito, 404 Not Found si no lo es
     */
    @GetMapping("/{id}")
    public ResponseEntity<Void> esFavorito(@PathVariable Long id) {
        if (usuarioService.esPeliculaFavorita(id)) {
            return ResponseEntity.ok().build(); // 200 OK - Es favorito
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found - No es favorito
        }
    }

    /**
     * Añadir una película a favoritos
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> agregarFavorito(@PathVariable Long id) {
        try {
            usuarioService.agregarPeliculaFavorita(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Eliminar una película de favoritos
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFavorito(@PathVariable Long id) {
        try {
            usuarioService.eliminarPeliculaFavorita(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}