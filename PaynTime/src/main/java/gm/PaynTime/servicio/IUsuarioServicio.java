package gm.PaynTime.servicio;

import gm.PaynTime.exepciones.UsuarioYaExisteException;
import gm.PaynTime.modelo.Usuario;

public interface IUsuarioServicio {
	
	public Usuario buscarUsuarioPorID(Integer ID);
	
	public void guardarUsuario(Usuario usuario) throws UsuarioYaExisteException;
	
	public void eliminarUsuario(Usuario usuario);
	
	// Métodos para login y registro
	public Usuario buscarPorEmail(String email);
	
	public Usuario buscarPorNombreUsuario(String nombreUsuario);
	
	public boolean validarLogin(String emailONombreUsuario, String password);
}
