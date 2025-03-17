package eus.birt.dam;

import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.repository.PeliculaRepository;
import eus.birt.dam.service.OmdbService;
import eus.birt.dam.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private OmdbService omdbService;

    @Autowired
    private PeliculaService peliculaService;
    
    @Autowired
    private PeliculaRepository peliculaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if we already have movies in the database
        List<Pelicula> existingMovies = peliculaRepository.findAll();
        if (!existingMovies.isEmpty()) {
            System.out.println("Database already contains " + existingMovies.size() + " movies. Skipping data loading.");
            return;
        }
        
        // Only load initial data if the database is empty
        String[] titulos = {"Inception", "Titanic", "Toy Story", "Matrix"};

        System.out.println("Loading initial movie data...");
        for (String titulo : titulos) {
            Pelicula peli = omdbService.obtenerPeliculaPorTitulo(titulo);
            if (peli != null) {
                peliculaService.guardarPelicula(peli);
                System.out.println("Insertada: " + peli.getTitulo());
            } else {
                System.out.println("No encontrada: " + titulo);
            }
        }
        System.out.println("Initial data loading complete.");
    }
}