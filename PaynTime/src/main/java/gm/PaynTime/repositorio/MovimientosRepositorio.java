package gm.PaynTime.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import gm.PaynTime.modelo.Movimiento;

public interface MovimientosRepositorio extends JpaRepository<Movimiento, Integer>{

}
