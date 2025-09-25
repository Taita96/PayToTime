package gm.PaynTime.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gm.PaynTime.modelo.Movimiento;
import gm.PaynTime.modelo.Usuario;

public interface MovimientosRepositorio extends JpaRepository<Movimiento, Integer>{
	
	public List<Movimiento> findByUsuario(Usuario user);
}
