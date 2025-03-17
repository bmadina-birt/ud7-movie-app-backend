package eus.birt.dam.dto;

public class ValoracionMediaDTO {
    private Double averageRating;
    
    public ValoracionMediaDTO() {
    }
    
    public ValoracionMediaDTO(Double averageRating) {
        this.averageRating = averageRating;
    }
    
    public Double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}