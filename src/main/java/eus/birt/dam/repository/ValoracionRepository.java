package eus.birt.dam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import eus.birt.dam.domain.Valoracion;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {
    List<Valoracion> findByPelicula_Id(Long peliculaId);
    
    Optional<Valoracion> findByUsuario_IdAndPelicula_Id(Long usuarioId, Long peliculaId);
}