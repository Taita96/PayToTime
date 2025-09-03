package gm.PaynTime.presentacion;

import java.io.IOException;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import gm.PaynTime.PaynTimeApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaynTimeFX extends Application {

	private ConfigurableApplicationContext applicantionContext;

	@Override
	public void init() {
		applicantionContext = new SpringApplicationBuilder(PaynTimeApplication.class).run();
	}

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(PaynTimeApplication.class.getResource("/templates/index.fxml"));

		loader.setControllerFactory(applicantionContext::getBean);

		Scene scene = new Scene(loader.load());
//		scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
		stage.setScene(scene);

		stage.show();

	}

	@Override
	public void stop() {
		applicantionContext.close();
		Platform.exit();
	}
}
