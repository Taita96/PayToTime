package gm.PaynTime.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gm.PaynTime.modelo.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
	
	Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
    boolean existsByNombreUsuario(String nombreUsuario);

}
