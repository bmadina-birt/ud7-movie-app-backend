package eus.birt.dam.service;

import eus.birt.dam.domain.Favorito;
import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.domain.Usuario;
import eus.birt.dam.repository.FavoritoRepository;
import eus.birt.dam.repository.PeliculaRepository;
import eus.birt.dam.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class FavoritoService {
    
    @Autowired
    private FavoritoRepository favoritoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PeliculaRepository peliculaRepository;

    /**
     * Obtiene todas las películas favoritas de un usuario
     * Devuelve lista vacía en lugar de null
     */
    public List<Pelicula> obtenerFavoritosPorUsuario(Long usuarioId) {
        if (usuarioId == null) {
            return Collections.emptyList();
        }
        return favoritoRepository.findByUsuario_Id(usuarioId)
                .stream()
                .map(Favorito::getPelicula)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si una película está en favoritos de un usuario
     */
    public boolean esFavorito(Long usuarioId, Long peliculaId) {
        if (usuarioId == null || peliculaId == null) {
            return false;
        }
        return favoritoRepository.existsByUsuario_IdAndPelicula_Id(usuarioId, peliculaId);
    }

    /**
     * Añade una película a favoritos de un usuario
     */
    @Transactional
    public Favorito agregarFavorito(Long usuarioId, Long peliculaId) {
        // Verificar parámetros
        if (usuarioId == null || peliculaId == null) {
            return null;
        }
        
        // Verificar si ya existe el favorito
        if (favoritoRepository.existsByUsuario_IdAndPelicula_Id(usuarioId, peliculaId)) {
            // Ya existe, retornamos el existente
            return favoritoRepository.findByUsuario_IdAndPelicula_Id(usuarioId, peliculaId).orElse(null);
        }
        
        // Buscar usuario y película
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        Optional<Pelicula> pelicula = peliculaRepository.findById(peliculaId);
        
        if (usuario.isPresent() && pelicula.isPresent()) {
            Favorito favorito = new Favorito(usuario.get(), pelicula.get());
            return favoritoRepository.save(favorito);
        }
        
        return null;
    }

    /**
     * Elimina una película de favoritos de un usuario
     */
    @Transactional
    public void eliminarFavorito(Long usuarioId, Long peliculaId) {
        if (usuarioId != null && peliculaId != null) {
            favoritoRepository.deleteByUsuario_IdAndPelicula_Id(usuarioId, peliculaId);
        }
    }
}