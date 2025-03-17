package eus.birt.dam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OmdbResponse {

    // Mapea campos del JSON de OMDb (ver la doc oficial)
    @JsonProperty("Title")
    private String title;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Poster")
    private String poster;

    @JsonProperty("Released")
    private String released; // "16 Jul 2010"

    @JsonProperty("Response")
    private String response;

    // getters y setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getPlot() { return plot; }
    public void setPlot(String plot) { this.plot = plot; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public String getReleased() { return released; }
    public void setReleased(String released) { this.released = released; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}
