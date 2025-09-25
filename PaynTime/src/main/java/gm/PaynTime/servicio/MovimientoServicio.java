package gm.PaynTime.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gm.PaynTime.modelo.Movimiento;
import gm.PaynTime.modelo.Usuario;
import gm.PaynTime.repositorio.MovimientosRepositorio;

@Service
public class MovimientoServicio implements IMovimientoServicio{
	
	@Autowired
	private MovimientosRepositorio movimientoRespositorio;
	
	@Override
	public List<Movimiento> listarMovimientos() {
		return movimientoRespositorio.findAll();
	}

	@Override
	public Movimiento buscarMovimientoPorID(Integer id) {
		return movimientoRespositorio.findById(id).orElse(null);
	}

	@Override
	public void guardarMovimientos(Movimiento movimiento) {
		movimientoRespositorio.save(movimiento);
	}

	@Override
	public void EliminarMovimientos(Movimiento movimiento) {
		movimientoRespositorio.delete(movimiento);
		
	}

	@Override
	public List<Movimiento> listarMovientos(Usuario session) {
		return movimientoRespositorio.findByUsuario(session);
	}
	
}
