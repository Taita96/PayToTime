package gm.PaynTime.servicio;

import java.util.List;

import org.springframework.stereotype.Service;

import gm.PaynTime.modelo.HoraExtra;
import gm.PaynTime.repositorio.HoraExtraRepositorio;

@Service
public class HoraExtraServicio implements IHoraExtraServicio{
	
	private HoraExtraRepositorio horaExtraRepositorio;
	
	@Override
	public List<HoraExtra> listarHorasExtras() {
		return horaExtraRepositorio.findAll();
	}

	@Override
	public HoraExtra buscarHoraExtraPorID(Integer id) {
		return horaExtraRepositorio.findById(id).orElse(null);
	}

	@Override
	public void guardarHoraExtra(HoraExtra horaExtra) {
		horaExtraRepositorio.save(horaExtra);
	}

	@Override
	public void eliminarHoraExtra(HoraExtra horaExtra) {
		horaExtraRepositorio.delete(horaExtra);
		
	}

}
