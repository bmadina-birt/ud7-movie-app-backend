package eus.birt.dam.mapper;

import eus.birt.dam.domain.Favorito;
import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.domain.Usuario;
import eus.birt.dam.domain.Valoracion;
import eus.birt.dam.dto.FavoritoDTO;
import eus.birt.dam.dto.PeliculaDTO;
import eus.birt.dam.dto.UsuarioDTO;
import eus.birt.dam.dto.UsuarioRegistroDTO;
import eus.birt.dam.dto.ValoracionDTO;

/**
 * Clase utilitaria para mapear entidades a DTOs y viceversa
 */
public class EntityDtoMapper {

    /**
     * Convierte una entidad Pelicula a PeliculaDTO
     */
    public static PeliculaDTO toPeliculaDTO(Pelicula pelicula) {
        if (pelicula == null) {
            return null;
        }
        
        PeliculaDTO dto = new PeliculaDTO();
        dto.setId(pelicula.getId());
        dto.setTitulo(pelicula.getTitulo());
        dto.setDescripcion(pelicula.getDescripcion());
        dto.setImagenUrl(pelicula.getImagenUrl());
        dto.setGenero(pelicula.getGenero());
        dto.setFechaEstreno(pelicula.getFechaEstreno());
        
        return dto;
    }

    /**
     * Convierte un DTO a entidad Pelicula
     * Útil para crear o actualizar entidades
     */
    public static void updatePeliculaFromDTO(PeliculaDTO dto, Pelicula pelicula) {
        if (dto == null || pelicula == null) {
            return;
        }
        
        pelicula.setTitulo(dto.getTitulo());
        pelicula.setDescripcion(dto.getDescripcion());
        pelicula.setImagenUrl(dto.getImagenUrl());
        pelicula.setGenero(dto.getGenero());
        pelicula.setFechaEstreno(dto.getFechaEstreno());
    }
    
    /**
     * Crea una nueva entidad Pelicula a partir de un DTO
     */
    public static Pelicula toPelicula(PeliculaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Pelicula pelicula = new Pelicula();
        updatePeliculaFromDTO(dto, pelicula);
        return pelicula;
    }
    
    /**
     * Convierte un Usuario a UsuarioDTO
     */
    public static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setEmail(usuario.getEmail());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        
        return dto;
    }
    
    /**
     * Convierte un DTO de registro a entidad Usuario
     */
    public static Usuario toUsuario(UsuarioRegistroDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword()); // La encriptación se hace en el servicio
        
        return usuario;
    }
    
    /**
     * Convierte un Favorito a FavoritoDTO con información extra
     */
    public static FavoritoDTO toFavoritoDTO(Favorito favorito) {
        if (favorito == null) {
            return null;
        }
        
        FavoritoDTO dto = new FavoritoDTO();
        dto.setId(favorito.getId());
        dto.setUsuarioId(favorito.getUsuario().getId());
        dto.setPeliculaId(favorito.getPelicula().getId());
        dto.setFechaAdicion(favorito.getFechaAdicion());
        
        // Incluir información adicional de la película
        if (favorito.getPelicula() != null) {
            dto.setTituloPelicula(favorito.getPelicula().getTitulo());
            dto.setImagenUrlPelicula(favorito.getPelicula().getImagenUrl());
        }
        
        return dto;
    }
    
    /**
     * Convierte una Valoracion a ValoracionDTO
     */
    public static ValoracionDTO toValoracionDTO(Valoracion valoracion) {
        if (valoracion == null) {
            return null;
        }
        
        ValoracionDTO dto = new ValoracionDTO();
        dto.setId(valoracion.getId());
        
        if (valoracion.getUsuario() != null) {
            dto.setUserId(valoracion.getUsuario().getId());
            dto.setNombreUsuario(valoracion.getUsuario().getNombreUsuario());
        }
        
        if (valoracion.getPelicula() != null) {
            dto.setPeliculaId(valoracion.getPelicula().getId());
        }
        
        dto.setRating(valoracion.getPuntuacion());
        dto.setComentario(valoracion.getComentario());
        dto.setFecha(valoracion.getFecha());
        
        return dto;
    }
}