package gm.PaynTime.servicio;

import java.util.List;

import gm.PaynTime.modelo.HoraExtra;

public interface IHoraExtraServicio {
	
	public List<HoraExtra> listarHorasExtras();
	
	public HoraExtra buscarHoraExtraPorID(Integer id);
	
	public void guardarHoraExtra(HoraExtra horaExtra);
	
	public void eliminarHoraExtra(HoraExtra horaExtra);
}
