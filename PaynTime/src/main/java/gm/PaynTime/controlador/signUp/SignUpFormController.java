package gm.PaynTime.controlador.signUp;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gm.PaynTime.servicio.IUsuarioServicio;
import gm.PaynTime.utilities.ControllerGeneralModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

@Component
public class SignUpFormController implements Initializable{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpFormController.class);

	
	@FXML
	private TextField txtEmail,txtUser;
	
	@FXML 
	private PasswordField txtPassword, txtConfirmPassword;
	
	@FXML
	private Button btnRegister, btnClean;
	
	//Servicio de dominio
	@Autowired
    private final IUsuarioServicio usuarioServicio;
    
	// Inyección por constructor (Spring la resuelve)
    public SignUpFormController(IUsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }
    


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("SignUpFormController inicializado");
        // Si quieres: añadir listeners, trim automático, etc.
    }
    
    
    @FXML
    private void onRegisterAction(){
    	String email = ControllerGeneralModel.trim(txtEmail.getText());
    	String user = ControllerGeneralModel.trim(txtUser.getText());
    	String password = txtPassword.getText();
    	String confirmPassword = txtConfirmPassword.getText();
    	
    	if(email.isEmpty() || user.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
    		ControllerGeneralModel.alert(Alert.AlertType.WARNING, "Campos obligatorios", "Completa todos los campos.");
            return;
    	}
    	
    	if (!ControllerGeneralModel.validateEmail(email)) {
    		ControllerGeneralModel.alert(Alert.AlertType.WARNING, "Email inválido", "Ingresa un correo electrónico válido.");
            txtEmail.requestFocus();
            return;
        }
    	
    	if (password.length() < 6) {
    		ControllerGeneralModel.alert(Alert.AlertType.WARNING, "Contraseña insegura", "La contraseña debe tener al menos 6 caracteres.");
            txtPassword.requestFocus();
            return;
        }
    	
    	if (!password.equals(confirmPassword)) {
    		ControllerGeneralModel.alert(Alert.AlertType.WARNING, "Contraseñas no coinciden", "Verifica la confirmación de contraseña.");
            txtConfirmPassword.requestFocus();
            return;
        }
    	
    }
    
    @FXML
    private void keyEvent(KeyEvent e){
    	String character = e.getCharacter();
    	
    	if(character.equals(" ")){
    		e.consume();
    	}
    }
    
    @FXML
    private void onCleanAction() {
        txtEmail.clear();
        txtUser.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        txtEmail.requestFocus();
    }
    
    

}
