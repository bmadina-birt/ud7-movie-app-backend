package eus.birt.dam.service;

import eus.birt.dam.domain.Usuario;
import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private FavoritoService favoritoService;

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // Cifra la contraseña
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    /**
     * Obtiene el usuario actualmente autenticado
     */
    public Usuario obtenerUsuarioActual() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof UserDetails) {
            String email = ((UserDetails)principal).getUsername();
            return obtenerPorEmail(email).orElse(null);
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las películas favoritas del usuario actual
     */
    public List<Pelicula> obtenerFavoritosUsuarioActual() {
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            return favoritoService.obtenerFavoritosPorUsuario(usuario.getId());
        }
        return List.of(); // Lista vacía si no hay usuario
    }
    
    /**
     * Verifica si una película es favorita del usuario actual
     */
    public boolean esPeliculaFavorita(Long peliculaId) {
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            return favoritoService.esFavorito(usuario.getId(), peliculaId);
        }
        return false;
    }
    
    /**
     * Añade una película a favoritos del usuario actual
     */
    public void agregarPeliculaFavorita(Long peliculaId) {
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            favoritoService.agregarFavorito(usuario.getId(), peliculaId);
        }
    }
    
    /**
     * Elimina una película de favoritos del usuario actual
     */
    public void eliminarPeliculaFavorita(Long peliculaId) {
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            favoritoService.eliminarFavorito(usuario.getId(), peliculaId);
        }
    }
}