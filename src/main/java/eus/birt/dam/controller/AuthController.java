package eus.birt.dam.controller;

import eus.birt.dam.config.security.JwtTokenProvider;
import eus.birt.dam.dto.AuthRequest;
import eus.birt.dam.dto.AuthResponse;
import eus.birt.dam.dto.UsuarioDTO;
import eus.birt.dam.mapper.EntityDtoMapper;
import eus.birt.dam.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
            // Si llega aquí, credenciales válidas
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Generamos el token
            String token = tokenProvider.generarToken(request.getEmail());
            
            // Obtenemos datos del usuario
            UsuarioDTO usuario = EntityDtoMapper.toUsuarioDTO(
                usuarioService.obtenerPorEmail(request.getEmail()).get()
            );
            
            // Devolvemos token y datos del usuario
            return ResponseEntity.ok(new AuthResponse(token, usuario));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
