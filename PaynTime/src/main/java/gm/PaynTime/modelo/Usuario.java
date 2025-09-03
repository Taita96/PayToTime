package gm.PaynTime.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUsuario;
	
	@Column(nullable = false)
	private String password; // Guardar solo el hash
	
	@Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nombreUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    
    private BigDecimal saldo = BigDecimal.ZERO; // Manejo seguro de dinero
    
//    @CreationTimestamp
//    private LocalDateTime fechaCreacion;
//
//    @UpdateTimestamp
//    private LocalDateTime fechaActualizacion;
	

    // Constructor personalizado sin password para evitar errores al crear el usuario
    public Usuario(String email, String nombreUsuario, String nombre, String apellido) {
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.saldo = BigDecimal.ZERO;
    }

    // Método para establecer la contraseña de forma segura (hash)
    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    // Método privado para encriptar la contraseña
    private String hashPassword(String password) {
        // Aquí puedes usar BCrypt u otro algoritmo
        return org.springframework.security.crypto.bcrypt.BCrypt.hashpw(password, org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
    }

    // Método para verificar la contraseña
    public boolean checkPassword(String password) {
        return org.springframework.security.crypto.bcrypt.BCrypt.checkpw(password, this.password);
    }
}
