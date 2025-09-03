package gm.PaynTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import gm.PaynTime.presentacion.PaynTimeFX;
import javafx.application.Application;

@SpringBootApplication
public class PaynTimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaynTimeApplication.class, args);
		
		Application.launch(PaynTimeFX.class, args);
	}

}
