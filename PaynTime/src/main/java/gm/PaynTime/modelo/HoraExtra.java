package gm.PaynTime.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HoraExtra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDateTime horaEntradaEstandar;
	private LocalDateTime horaSalidaEstandar;
	private LocalDateTime horaRealEntrada;
	private LocalDateTime horaRealsalida;

	private double calculoHorasExtras;
	private double horasTotalTrabajadas;
	
	@ManyToOne
	private Usuario usuario;

}
