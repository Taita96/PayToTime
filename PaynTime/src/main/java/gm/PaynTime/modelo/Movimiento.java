package gm.PaynTime.modelo;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.Lob;
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
public class Movimiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private LocalDate fecha;
	private String tipo;
	private double monto;
	private String Descripcion;
	private int usuarioLogeado;
	
	
//	@Column(name = "foto", columnDefinition = "BLOB")
//	private byte[] foto;
	
	private String rutaFoto;
	
	@ManyToOne
	private Usuario usuario;
	
}
