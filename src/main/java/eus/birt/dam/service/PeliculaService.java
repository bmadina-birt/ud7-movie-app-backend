package eus.birt.dam.service;

import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaService {
    @Autowired
    private PeliculaRepository peliculaRepository;

    public List<Pelicula> obtenerTodas() {
        return peliculaRepository.findAll();
    }

    public Optional<Pelicula> obtenerPorId(Long id) {
        return peliculaRepository.findById(id);
    }

    public Pelicula agregarPelicula(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    public void eliminarPelicula(Long id) {
        peliculaRepository.deleteById(id);
    }

    public Pelicula guardarPelicula(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }
    
    // Nuevo método para búsqueda general
    public List<Pelicula> buscarPeliculas(String query) {
        if (query == null || query.isEmpty()) {
            return obtenerTodas();
        }
        return peliculaRepository.findByTituloOrGeneroContaining(query);
    }
    
    // Buscar por título específicamente
    public List<Pelicula> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            return obtenerTodas();
        }
        return peliculaRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    // Buscar por género específicamente
    public List<Pelicula> buscarPorGenero(String genero) {
        if (genero == null || genero.isEmpty()) {
            return obtenerTodas();
        }
        return peliculaRepository.findByGeneroContainingIgnoreCase(genero);
    }
}