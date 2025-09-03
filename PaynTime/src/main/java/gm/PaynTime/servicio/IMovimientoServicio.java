package gm.PaynTime.servicio;

import java.util.List;

import gm.PaynTime.modelo.Movimiento;

public interface IMovimientoServicio {
	
	public List<Movimiento> listarMovimientos();
	
	public Movimiento buscarMovimientoPorID(Integer id);
	
	public void guardarMovimientos(Movimiento movimiento);
	
	public void EliminarMovimientos(Movimiento movimiento);
	
}
