package eus.birt.dam.service;

import eus.birt.dam.domain.Valoracion;
import eus.birt.dam.repository.ValoracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ValoracionService {
    @Autowired
    private ValoracionRepository valoracionRepository;

    /**
     * Obtiene todas las valoraciones de una película
     */
    public List<Valoracion> obtenerValoracionesPorPelicula(Long peliculaId) {
        return valoracionRepository.findByPelicula_Id(peliculaId);
    }

    /**
     * Guarda una nueva valoración
     */
    public Valoracion guardarValoracion(Valoracion valoracion) {
        return valoracionRepository.save(valoracion);
    }
    
    /**
     * Calcula la valoración media de una película
     */
    public Double calcularValoracionMedia(Long peliculaId) {
        List<Valoracion> valoraciones = valoracionRepository.findByPelicula_Id(peliculaId);
        if (valoraciones.isEmpty()) {
            return 0.0;
        }
        
        double suma = valoraciones.stream()
                .mapToInt(Valoracion::getPuntuacion)
                .sum();
        
        return suma / valoraciones.size();
    }
    
    /**
     * Obtiene la valoración de un usuario para una película específica
     */
    public Optional<Valoracion> obtenerValoracionPorUsuarioYPelicula(Long usuarioId, Long peliculaId) {
        return valoracionRepository.findByUsuario_IdAndPelicula_Id(usuarioId, peliculaId);
    }
    
    /**
     * Elimina la valoración de un usuario para una película
     */
    @Transactional
    public boolean eliminarValoracionPorUsuarioYPelicula(Long usuarioId, Long peliculaId) {
        Optional<Valoracion> valoracion = valoracionRepository.findByUsuario_IdAndPelicula_Id(usuarioId, peliculaId);
        if (valoracion.isPresent()) {
            valoracionRepository.delete(valoracion.get());
            return true;
        }
        return false;
    }
}