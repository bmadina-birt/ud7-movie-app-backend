package eus.birt.dam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import eus.birt.dam.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
