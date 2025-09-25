package gm.PaynTime.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gm.PaynTime.modelo.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
	
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByNombreUsuarioAndPassword(String nombreUsuario, String password);
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    
//    @Query("SELECT u FROM Usuario u " +
//            "WHERE (u.email = :login OR u.nombreUsuario = :login) " +
//            "AND u.password = :password")
//     Optional<Usuario> login(@Param("login") String login, @Param("password") String password);

    boolean existsByEmail(String email);
    boolean existsByNombreUsuario(String nombreUsuario);

}
