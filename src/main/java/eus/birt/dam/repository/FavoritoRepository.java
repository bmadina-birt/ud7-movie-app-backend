package eus.birt.dam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import eus.birt.dam.domain.Favorito;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuario_Id(Long usuarioId);
    Optional<Favorito> findByUsuario_IdAndPelicula_Id(Long usuarioId, Long peliculaId);
    boolean existsByUsuario_IdAndPelicula_Id(Long usuarioId, Long peliculaId);
    
    @Modifying
    @Transactional
    void deleteByUsuario_IdAndPelicula_Id(Long usuarioId, Long peliculaId);
}