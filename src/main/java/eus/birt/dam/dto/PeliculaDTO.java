package eus.birt.dam.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PeliculaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private String genero;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaEstreno;
    
    // Campos calculados que pueden agregarse según necesidad
    private Double valoracionMedia;
    private Boolean esFavorito;
    
    // Constructores
    public PeliculaDTO() {
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getImagenUrl() {
        return imagenUrl;
    }
    
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }
    
    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }
    
    public Double getValoracionMedia() {
        return valoracionMedia;
    }
    
    public void setValoracionMedia(Double valoracionMedia) {
        this.valoracionMedia = valoracionMedia;
    }
    
    public Boolean getEsFavorito() {
        return esFavorito;
    }
    
    public void setEsFavorito(Boolean esFavorito) {
        this.esFavorito = esFavorito;
    }
}