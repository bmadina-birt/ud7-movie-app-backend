package eus.birt.dam.service;

import eus.birt.dam.domain.Pelicula;
import eus.birt.dam.dto.OmdbResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class OmdbService {

    // Inyectamos la clave que definimos en application.properties
    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Llama a la API de OMDb buscando por título.
     * @param titulo Título de la película (Ej: "Inception")
     * @return Pelicula mapeada, o null si no se encontró
     */
    public Pelicula obtenerPeliculaPorTitulo(String titulo) {
        // Construimos la URL para OMDb
        // Ejemplo: http://www.omdbapi.com/?apikey=XXXXX&t=Inception
        String url = String.format("http://www.omdbapi.com/?apikey=%s&t=%s", omdbApiKey, titulo);

        // Realizamos la petición GET y parseamos la respuesta en un Map genérico
        var respuesta = restTemplate.getForObject(url, OmdbResponse.class);

        // Verificamos si la respuesta es válida
        if (respuesta != null && respuesta.getResponse().equalsIgnoreCase("True")) {
            // Convertimos la respuesta en un objeto Pelicula
            return mapearOmdbAPelicula(respuesta);
        } else {
            return null; // No se encontró
        }
    }

    // Convierte el objeto OmdbResponse (más abajo) en una entidad Pelicula
    private Pelicula mapearOmdbAPelicula(OmdbResponse o) {
        Pelicula p = new Pelicula();
        p.setTitulo(o.getTitle());
        p.setDescripcion(o.getPlot());
        p.setGenero(o.getGenre());
        p.setImagenUrl(o.getPoster());
        // Intenta parsear la fecha de estreno si existe
        // Por ejemplo, "Released": "16 Jul 2010"
        // Podrías necesitar formatear la fecha manualmente o solo quedarte con el año
        p.setFechaEstreno(parseFecha(o.getReleased()));
        return p;
    }

    private LocalDate parseFecha(String released) {
        // Ejemplo rápido: si el released = "16 Jul 2010"
        // Lo convertimos a un LocalDate con parseos más elaborados
        // Para la demo, lo hacemos muy simplificado:
        try {
            String[] partes = released.split(" ");
            int dia = Integer.parseInt(partes[0]);
            String mesTexto = partes[1];
            int ano = Integer.parseInt(partes[2]);
            int mes = convertirMes(mesTexto);
            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            return null; // Si no se puede parsear, devuelve null
        }
    }

    private int convertirMes(String mesTexto) {
        // "Jan", "Feb", "Mar", etc.
        return switch (mesTexto) {
            case "Jan" -> 1; case "Feb" -> 2; case "Mar" -> 3; case "Apr" -> 4;
            case "May" -> 5; case "Jun" -> 6; case "Jul" -> 7; case "Aug" -> 8;
            case "Sep" -> 9; case "Oct" -> 10; case "Nov" -> 11; case "Dec" -> 12;
            default -> 1;
        };
    }
}
