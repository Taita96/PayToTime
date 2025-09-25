package gm.PaynTime.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gm.PaynTime.utilities.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Component
public class IndexControlador implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexControlador.class);

    @FXML
    private Button btnSignIn, btnSignUp;

    @FXML
    private StackPane containerForm;

    private VBox signInForm, signUpForm;

    @Autowired
    private SpringFXMLLoader fxmlLoader; // Loader que integra Spring con JavaFX

    @FXML
    public void actionEvent(ActionEvent e) {
        Object evt = e.getSource();
        Stage stage = (Stage) containerForm.getScene().getWindow();

        
        if (evt.equals(btnSignIn)) {
            signInForm.setVisible(true);
            stage.setTitle("Inicio Sesión");
            signUpForm.setVisible(false);
        } else if (evt.equals(btnSignUp)) {
            signUpForm.setVisible(true);
            stage.setTitle("Registrar");
            signInForm.setVisible(false);
        }
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            // Cargar los formularios con el loader de Spring
            signInForm = fxmlLoader.load("signIn.fxml");
            signUpForm = fxmlLoader.load("signUp.fxml");

            containerForm.getChildren().addAll(signInForm, signUpForm);

            // Mostrar solo el formulario de Sign In por defecto
            signInForm.setVisible(true);
            signUpForm.setVisible(false);


        } catch (IOException ex) {
            LOGGER.error("Error al cargar formulario", ex);
        }
    }
}
