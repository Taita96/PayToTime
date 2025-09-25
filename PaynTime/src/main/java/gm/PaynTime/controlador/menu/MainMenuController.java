package gm.PaynTime.controlador.menu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import gm.PaynTime.controlador.IndexControlador;
import gm.PaynTime.modelo.Movimiento;
import gm.PaynTime.modelo.Usuario;
import gm.PaynTime.servicio.MovimientoServicio;
import gm.PaynTime.servicio.SessionService;
import gm.PaynTime.utilities.SpringFXMLLoader;

@Controller
public class MainMenuController implements Initializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexControlador.class);

	@FXML
	private Button btnIngresarGasto, btnHorasExtras, btnCerrarSesion;

	@FXML
	private Label lblSaldoDisponible;

	@FXML
	private TableView<Movimiento> tablaGastos;

	@FXML
	private TableColumn<Movimiento, Integer> colId;
	@FXML
	private TableColumn<Movimiento, LocalDate> colFecha;
	@FXML
	private TableColumn<Movimiento, String> colDescripcion;
	@FXML
	private TableColumn<Movimiento, String> colTipo;
	@FXML
	private TableColumn<Movimiento, Double> colMonto;
	@FXML
	private TableColumn<Movimiento, String> colFoto;
	@Autowired
	private MovimientoServicio movimientoServicio;
	@Autowired
	private SessionService sessionService;

	@FXML
	private BorderPane movimientos;

	private final ObservableList<Movimiento> movimientoList = FXCollections.observableArrayList();

	@Autowired
	private SpringFXMLLoader fxmlLoader; // Loader que integra Spring con JavaFX

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		LOGGER.info("\nSignInFormController inicializado\n");
		LOGGER.info("\nCliente Inicia sesion menu: " + sessionService.getUsuarioLogueado().getNombreUsuario()
				+ " con ID: " + sessionService.getUsuarioLogueado().getIdUsuario());
		lblSaldoDisponible.setText("0 €");

		listarMovimientos();
		configurarColumnas();
		actualizarSaldo();
	}

	private void actualizarSaldo() {
		double saldo = 0;
		Usuario usuario = sessionService.getUsuarioLogueado();
		List<Movimiento> movimiento = movimientoServicio.listarMovientos(usuario);

		for (Movimiento losMovimientos : movimiento) {
			if (losMovimientos.getTipo().equalsIgnoreCase("ingreso")) {
				saldo += losMovimientos.getMonto();
			} else {
				saldo -= losMovimientos.getMonto();
			}
		}

		lblSaldoDisponible.setText(String.format("%.2f €", saldo));
	}

	private void configurarColumnas() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		colDescripcion.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
		colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
		colFoto.setCellValueFactory(new PropertyValueFactory<>("rutaFoto"));

		colFoto.setCellFactory(column -> new javafx.scene.control.TableCell<Movimiento, String>() {
			private final Button btnVer = new Button("Ver");

			{
				// Botón Ver

				btnVer.setOnAction(event -> {
					Movimiento movimiento = getTableView().getItems().get(getIndex());
					String path = movimiento.getRutaFoto();

					if (path != null && !path.isEmpty()) {
						File file = new File(path);
						if (file.exists()) {
							ImageView imageView = new ImageView(new javafx.scene.image.Image("file:" + path));
							imageView.setFitWidth(400);
							imageView.setPreserveRatio(true);
							Stage stage = new Stage();
							VBox root = new VBox(imageView);
							root.setPadding(new javafx.geometry.Insets(10));
							Scene scene = new Scene(root);
							stage.setScene(scene);
							stage.setTitle("Imagen");
							stage.show();
						} else {
							Alert alert = new Alert(Alert.AlertType.WARNING,
									"No se encontró la imagen en la ruta:\n" + path);
							alert.showAndWait();
						}
					}
				});
			}

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setGraphic(null);
				} else {
					setGraphic(btnVer);
					setAlignment(Pos.CENTER);
				}
			}
		});

	}

	@FXML
	private void OnRegistrarMovimientos(ActionEvent e) {
		Object event = e.getSource();

		if (event.equals(btnIngresarGasto)) {
			try {
				Scene scene = new Scene(fxmlLoader.load("movimientos.fxml"));
				Stage stage = (Stage) btnIngresarGasto.getScene().getWindow();
				stage.setScene(scene);
				stage.centerOnScreen(); // para centrar
				stage.setTitle("Registrar Movimientos");
				stage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@FXML
	private void onCerrarSesion(ActionEvent e) {
		Object event = e.getSource();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que deseas cerrar sesión?", ButtonType.YES,
				ButtonType.NO);
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.YES) {

				if (event.equals(btnCerrarSesion)) {
					try {
						LOGGER.info("Usuario {} cerró sesión", sessionService.getUsuarioLogueado().getNombreUsuario());
						sessionService.setUsuarioLogueado(null);
						movimientoList.clear();
						tablaGastos.getItems().clear();
						lblSaldoDisponible.setText("0 €");

						Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
						stage.close();

						Stage loginStage = new Stage();
						loginStage.setScene(new Scene(fxmlLoader.load("index.fxml")));
						loginStage.setTitle("Iniciar Sesión");
						loginStage.show();

					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});

	}

	private void listarMovimientos() {
		LOGGER.info("\nEjecutando estado de Movimientos");
		movimientoList.clear();
		Usuario usuario = sessionService.getUsuarioLogueado();
		movimientoList.addAll(movimientoServicio.listarMovientos(usuario));
		tablaGastos.setItems(movimientoList);
	}
}
