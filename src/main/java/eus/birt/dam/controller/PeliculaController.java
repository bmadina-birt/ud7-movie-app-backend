package eus.birt.dam.controller;

import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.dto.PeliculaDTO;
import eus.birt.dam.mapper.EntityDtoMapper;
import eus.birt.dam.service.PeliculaService;
import eus.birt.dam.service.OmdbService;
import eus.birt.dam.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/peliculas")
@CrossOrigin(origins = "*") // Añadido para permitir CORS
public class PeliculaController {
    @Autowired
    private PeliculaService peliculaService;
    
    @Autowired
    private OmdbService omdbService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    // Obtener todas las películas (GET) - Modificado para usar DTOs
    @GetMapping
    public ResponseEntity<List<PeliculaDTO>> obtenerPeliculas(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String genero) {
        
        // Obtener películas según los parámetros
        List<Pelicula> peliculas;
        
        if (query != null && !query.isEmpty()) {
            peliculas = peliculaService.buscarPeliculas(query);
        } else if (titulo != null && !titulo.isEmpty()) {
            peliculas = peliculaService.buscarPorTitulo(titulo);
        } else if (genero != null && !genero.isEmpty()) {
            peliculas = peliculaService.buscarPorGenero(genero);
        } else {
            peliculas = peliculaService.obtenerTodas();
        }
        
        // Convertir entidades a DTOs
        List<PeliculaDTO> peliculaDTOs = peliculas.stream()
            .map(p -> {
                PeliculaDTO dto = EntityDtoMapper.toPeliculaDTO(p);
                
                // Enriquecer con información adicional si el usuario está autenticado
                try {
                    // Verificar si es favorito del usuario actual
                    if (usuarioService.obtenerUsuarioActual() != null) {
                        dto.setEsFavorito(usuarioService.esPeliculaFavorita(p.getId()));
                    }
                } catch (Exception e) {
                    // Si hay algún error, simplemente no se establece el campo esFavorito
                }
                
                return dto;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(peliculaDTOs);
    }

    // Obtener película por ID (GET) - Modificado para usar DTOs
    @GetMapping("/{id}")
    public ResponseEntity<PeliculaDTO> obtenerPorId(@PathVariable Long id) {
        Optional<Pelicula> pelicula = peliculaService.obtenerPorId(id);
        
        if (pelicula.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        PeliculaDTO dto = EntityDtoMapper.toPeliculaDTO(pelicula.get());
        
        // Enriquecer con información adicional
        try {
            // Verificar si es favorito del usuario actual
            if (usuarioService.obtenerUsuarioActual() != null) {
                dto.setEsFavorito(usuarioService.esPeliculaFavorita(id));
            }
        } catch (Exception e) {
            // Si hay algún error, simplemente no se establece el campo esFavorito
        }
        
        return ResponseEntity.ok(dto);
    }

    // Agregar nueva película (POST) - Modificado para usar DTOs
    @PostMapping
    public ResponseEntity<PeliculaDTO> agregarPelicula(@RequestBody PeliculaDTO peliculaDTO) {
        try {
            Pelicula nuevaPelicula = EntityDtoMapper.toPelicula(peliculaDTO);
            Pelicula guardada = peliculaService.agregarPelicula(nuevaPelicula);
            return ResponseEntity.status(HttpStatus.CREATED).body(EntityDtoMapper.toPeliculaDTO(guardada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Eliminar película por ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPelicula(@PathVariable Long id) {
        if (!peliculaService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        peliculaService.eliminarPelicula(id);
        return ResponseEntity.noContent().build();
    }

    // Importar película por título (GET)
    @GetMapping("/importar")
    public ResponseEntity<PeliculaDTO> importarPelicula(@RequestParam("t") String titulo) {
        Pelicula peli = omdbService.obtenerPeliculaPorTitulo(titulo);
        if (peli != null) {
            Pelicula guardada = peliculaService.guardarPelicula(peli);
            return ResponseEntity.ok(EntityDtoMapper.toPeliculaDTO(guardada));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}