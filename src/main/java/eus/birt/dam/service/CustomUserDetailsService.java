package eus.birt.dam.service;

import eus.birt.dam.domain.Usuario;
import eus.birt.dam.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca al usuario por email
        Usuario user = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Retornamos un objeto propio de Spring Security
        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())     // Ojo: aquí debe estar encriptada con BCrypt
                .authorities("USER")             // Roles básicos
                .build();
    }
}
