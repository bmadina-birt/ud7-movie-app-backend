package eus.birt.dam.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Ignora propiedades desconocidas y permite fallos en la deserialización
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
public class ValoracionDTO {
    private Long id;
    // Estos campos pueden ser nulos
    private Long userId;    
    private Long peliculaId;
    private Integer rating;
    private String comentario;
    private LocalDateTime fecha;
    private String nombreUsuario;
    
    // Constructores
    public ValoracionDTO() {
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getPeliculaId() {
        return peliculaId;
    }
    
    public void setPeliculaId(Long peliculaId) {
        this.peliculaId = peliculaId;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComentario() {
        return comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}