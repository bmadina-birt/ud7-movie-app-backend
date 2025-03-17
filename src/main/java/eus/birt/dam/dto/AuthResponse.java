package eus.birt.dam.dto;

/**
 * DTO para responder con el token JWT y datos del usuario tras la autenticación
 */
public class AuthResponse {
    private String token;
    private UsuarioDTO usuario;

    // Constructores
    public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
    }
    
    public AuthResponse(String token, UsuarioDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public UsuarioDTO getUsuario() {
        return usuario;
    }
    
    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
}
