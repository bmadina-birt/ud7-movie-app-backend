package eus.birt.dam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import eus.birt.dam.domain.Pelicula;

import java.util.List;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    // Buscar películas por título (insensible a mayúsculas/minúsculas)
    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);
    
    // Buscar películas por género (insensible a mayúsculas/minúsculas)
    List<Pelicula> findByGeneroContainingIgnoreCase(String genero);
    
    // Buscar películas por título o género (combinado)
    @Query("SELECT p FROM Pelicula p WHERE LOWER(p.titulo) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.genero) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Pelicula> findByTituloOrGeneroContaining(@Param("query") String query);
}