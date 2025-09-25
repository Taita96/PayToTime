package gm.PaynTime.servicio;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gm.PaynTime.exepciones.UsuarioNoExisteException;
import gm.PaynTime.exepciones.UsuarioYaExisteException;
import gm.PaynTime.modelo.Usuario;
import gm.PaynTime.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicio implements IUsuarioServicio {

//	@Autowired En Spring Boot moderno, si la clase tiene un solo constructor, no necesitas @Autowired explícitamente; Spring lo detecta automáticamente y lo inyecta.
	private UsuarioRepositorio usuarioRepositorio;

	public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Override
	public Usuario buscarUsuarioPorID(Integer id) {
		return usuarioRepositorio.findById(id).orElse(null);
	}

	@Override
	public void guardarUsuario(Usuario usuario) throws UsuarioYaExisteException {
		if (usuarioRepositorio.existsByEmail(usuario.getEmail())) {
			throw new UsuarioYaExisteException("Email ya registrado");
		}
		if (usuarioRepositorio.existsByNombreUsuario(usuario.getNombreUsuario())) {
			throw new UsuarioYaExisteException("Nombre de usuario ya registrado");
		}
		usuarioRepositorio.save(usuario);
	}

	@Override
	public void eliminarUsuario(Usuario usuario) {
		usuarioRepositorio.deleteById(usuario.getIdUsuario());
	}

	@Override
	public Usuario buscarPorEmail(String email) {
		return usuarioRepositorio.findByEmail(email).orElse(null);
	}

	@Override
	public Usuario buscarPorNombreUsuario(String nombreUsuario) {
		return usuarioRepositorio.findByNombreUsuario(nombreUsuario).orElse(null);
	}

	@Override
	public boolean validarLogin(String emailONombreUsuario, String password) throws UsuarioNoExisteException {
		Usuario usuario = usuarioRepositorio.findByEmail(emailONombreUsuario)
				.orElse(usuarioRepositorio.findByNombreUsuario(emailONombreUsuario).orElse(null));

		return usuario != null && usuario.checkPassword(password);
	}

	@Override
	public Usuario login(String username, String password) {
		return usuarioRepositorio.findByNombreUsuarioAndPassword(username, password).orElse(null);
	}

	@Override
	public Usuario buscarPorNombreUsuarioOEmil(String nombreUsuario, String filter) {
		Usuario cuenta = null;
		switch (filter) {

		case "email":
			cuenta = usuarioRepositorio.findByEmail(nombreUsuario).orElse(null);
			break;

		default:
			cuenta = usuarioRepositorio.findByNombreUsuario(nombreUsuario).orElse(null);
			break;

		}
		return cuenta;
	}

}
