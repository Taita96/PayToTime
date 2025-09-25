package gm.PaynTime.controlador.signIn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import gm.PaynTime.controlador.movimiento.MovimientoFormController;
import gm.PaynTime.modelo.Usuario;
import gm.PaynTime.servicio.IUsuarioServicio;
import gm.PaynTime.servicio.SessionService;
import gm.PaynTime.utilities.ControllerGeneralModel;
import gm.PaynTime.utilities.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@Controller
public class SignInFormController implements Initializable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SignInFormController.class);

	@FXML
	private TextField txtUserSignIn, txtPasswordSignInMask;

	@FXML
	private PasswordField txtPasswordSignIn;

	@FXML
	private CheckBox checkViewPassSignIn;

	@FXML
	private Button btnSignIn, btnClean;
	
	@FXML
	private BorderPane mainMenu;

	@Autowired
	private IUsuarioServicio usuarioServicio;
	
	@Autowired
    private SpringFXMLLoader fxmlLoader; // Loader que integra Spring con JavaFX
	
	@Autowired
	private SessionService sessionService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("SignInFormController inicializado");
		
		maskPassword(txtPasswordSignIn, txtPasswordSignInMask, checkViewPassSignIn);

	}

	public void maskPassword(PasswordField txtPasswordSignIn, TextField txtPasswordSignInMask,
			CheckBox checkViewPassSignIn) {

		txtPasswordSignInMask.setVisible(false);
		txtPasswordSignInMask.setManaged(false);

		txtPasswordSignInMask.managedProperty().bind(checkViewPassSignIn.selectedProperty());
		txtPasswordSignInMask.visibleProperty().bind(checkViewPassSignIn.selectedProperty());

		txtPasswordSignInMask.textProperty().bindBidirectional(txtPasswordSignIn.textProperty());

	}

	@FXML
	private void onSingInAction(ActionEvent e) {
		Object event = e.getSource();

		if (event.equals(btnSignIn)) {
			String login = ControllerGeneralModel.trim(txtUserSignIn.getText());
			String password = ControllerGeneralModel.trim(
					checkViewPassSignIn.isSelected() ? txtPasswordSignInMask.getText() : txtPasswordSignIn.getText());

			if (login.isEmpty() || password.isEmpty()) {
				ControllerGeneralModel.alert(AlertType.ERROR, "Error", "Deber rellenar Todos los Campos Obligatorios");
				return;
			}

			String filter = ControllerGeneralModel.validateEmail(login) ? "email" : "user";

			Usuario account = usuarioServicio.buscarPorNombreUsuarioOEmil(login, filter);

			if (account == null) {
				ControllerGeneralModel.alert(AlertType.ERROR, "ERROR", "El usuario no existe");
				return;
			}
			
			if(account.checkPassword(password)){
				ControllerGeneralModel.alert(AlertType.INFORMATION, "Bienvenido", "Inicio Exitoso");
				onCleanAction();
				
				sessionService.setUsuarioLogueado(account);
				LOGGER.info("\nCliente Inicia sesion: " + sessionService.getUsuarioLogueado().getNombreUsuario() + " con ID: " + sessionService.getUsuarioLogueado().getIdUsuario());
				
				try {
		            Scene scene = new Scene(fxmlLoader.load("mainMenu.fxml"));
		            Stage stage = (Stage) btnSignIn.getScene().getWindow();
		            stage.setScene(scene);
		            stage.centerOnScreen();   // para centrar
		            stage.setTitle("Menú Principal"); 
		            stage.show();
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
				 

			}else{
				ControllerGeneralModel.alert(AlertType.WARNING, "Contraseña incorrecta", "La contraseña ingresada no es correcta");
			}
		}
	}

	@FXML
	private void onCleanAction() {
		txtUserSignIn.clear();
		txtPasswordSignInMask.clear();
		txtPasswordSignIn.clear();
	}

}
