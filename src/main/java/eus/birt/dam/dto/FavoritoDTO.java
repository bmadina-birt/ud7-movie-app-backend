package eus.birt.dam.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FavoritoDTO {
    private Long id;
    private Long usuarioId;
    private Long peliculaId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaAdicion;
    
    // Información adicional sobre la película (para evitar múltiples llamadas)
    private String tituloPelicula;
    private String imagenUrlPelicula;

    // Constructores
    public FavoritoDTO() {
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getPeliculaId() {
        return peliculaId;
    }

    public void setPeliculaId(Long peliculaId) {
        this.peliculaId = peliculaId;
    }

    public LocalDateTime getFechaAdicion() {
        return fechaAdicion;
    }

    public void setFechaAdicion(LocalDateTime fechaAdicion) {
        this.fechaAdicion = fechaAdicion;
    }

    public String getTituloPelicula() {
        return tituloPelicula;
    }

    public void setTituloPelicula(String tituloPelicula) {
        this.tituloPelicula = tituloPelicula;
    }

    public String getImagenUrlPelicula() {
        return imagenUrlPelicula;
    }

    public void setImagenUrlPelicula(String imagenUrlPelicula) {
        this.imagenUrlPelicula = imagenUrlPelicula;
    }
}