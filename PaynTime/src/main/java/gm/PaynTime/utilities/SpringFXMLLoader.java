package gm.PaynTime.utilities;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
//import java.net.URL;

@Component
public class SpringFXMLLoader {

    private final ApplicationContext context;

    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    public <T> T load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/templates/" + fxmlPath)); // si no hubico el templates lo buscará prederteminadamente por la carpeta view
        loader.setControllerFactory(context::getBean); // <-- clave para Spring
        return loader.load();
    }
}
