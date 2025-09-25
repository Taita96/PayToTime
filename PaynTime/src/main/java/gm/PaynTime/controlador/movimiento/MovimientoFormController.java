package gm.PaynTime.controlador.movimiento;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;

import gm.PaynTime.controlador.IndexControlador;
import gm.PaynTime.modelo.Movimiento;
import gm.PaynTime.modelo.Usuario;
import gm.PaynTime.servicio.FotoService;
import gm.PaynTime.servicio.IMovimientoServicio;
import gm.PaynTime.servicio.MovimientoServicio;
import gm.PaynTime.servicio.SessionService;
import gm.PaynTime.servicio.UsuarioServicio;
import gm.PaynTime.utilities.ControllerGeneralModel;
import gm.PaynTime.utilities.SpringFXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@Controller
public class MovimientoFormController implements Initializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovimientoFormController.class);

	public static final String DIRECTORIO_FOTOS = System.getProperty("user.home") + File.separator + "PaynTime"
			+ File.separator + "fotos";

	@FXML
	private DatePicker datePicker;

	@FXML
	private TextField textDescripcion, textMonto;

	@FXML
	private Button btnCargarImagen, btnGuardar, btnBorrar, btnClean, btnMenuPrincipal, btnModificar;

	@FXML
	private ComboBox<String> comboTipo;

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

	private final ObservableList<Movimiento> movimientoList = FXCollections.observableArrayList();

	private String fotoRutaSeleccionada;

//	private File imagenSeleccionada;

	private Integer idMovimientoInterno;

	@Autowired
	private MovimientoServicio movimientoServicio;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private SpringFXMLLoader fxmlLoader;

	@Autowired
	private FotoService fotoService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LOGGER.info("\nInicia MovimientoFormController\n");
		LOGGER.info("\nCliente Inicia sesion menu: " + sessionService.getUsuarioLogueado().getNombreUsuario() + " con ID: " + sessionService.getUsuarioLogueado().getIdUsuario());
		LocalDate fecha = LocalDate.now();
		datePicker.setPromptText(fecha.toString());
		comboTipo.setItems(FXCollections.observableArrayList("Ingreso", "Egreso"));
		tablaGastos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		listarMovimientos();
		configurarColumnas();

		tablaGastos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				cargarMovimientoEnFormulario(newSelection);
			}
		});

	}

	@FXML
	public void agregarMovimiento() {

		boolean algunCampoVacio = datePicker.getValue() == null || textMonto.getText().isEmpty()
				|| comboTipo.getValue().isEmpty();

		if (algunCampoVacio) {
			ControllerGeneralModel.alert(Alert.AlertType.WARNING, "Error Validacion", "Faltan campos por añadir");
			datePicker.requestFocus();
			return;
		}

		Movimiento movimientos = new Movimiento();
		recolectarDatosFormulario(movimientos);
		movimientoServicio.guardarMovimientos(movimientos);
		ControllerGeneralModel.alert(AlertType.INFORMATION, "Guardo", "Movimiento Registrado");
		onLimpiarFormulario();
		listarMovimientos();

	}

	private void configurarColumnas() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		colDescripcion.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
		colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
		colFoto.setCellValueFactory(new PropertyValueFactory<>("rutaFoto"));

		colFoto.setCellFactory(column -> new javafx.scene.control.TableCell<Movimiento, String>() {
			 private final Button btnAgregar = new Button("Agregar Imagen");
			 private final Button btnVer = new Button("Ver");
			 private final Button btnEliminar = new Button("Eliminar");
			 private final HBox hboxConFoto = new HBox(5, btnVer, btnEliminar);


			 { 
				 hboxConFoto.setAlignment(Pos.CENTER);
				 
			        // Botón Agregar Imagen
			        btnAgregar.setOnAction(event -> {
			            Movimiento movimiento = getTableView().getItems().get(getIndex());
			            FileChooser fileChooser = new FileChooser();
			            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
			            File file = fileChooser.showOpenDialog(null);
			            if (file != null) {
			                try {
			                    String rutaGuardada = fotoService.guardarFoto(file, movimiento.getId());
			                    movimiento.setRutaFoto(rutaGuardada);
			                    movimientoServicio.guardarMovimientos(movimiento); // actualizar DB
			                    getTableView().refresh(); // refresca la fila para mostrar botones Ver/Eliminar
			                } catch (IOException e) {
			                    e.printStackTrace();
			                    ControllerGeneralModel.alert(AlertType.ERROR, "Error", "No se pudo guardar la imagen");
			                }
			            }
			        });

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
			                    stage.setTitle(file.getName()); // Nombre de la imagen como título
			                    stage.show();
			                }
			            }
			        });

			        // Botón Eliminar
			        btnEliminar.setOnAction(event -> {
			            Movimiento movimiento = getTableView().getItems().get(getIndex());
			            String rutaFoto = movimiento.getRutaFoto();
			            if (rutaFoto != null) {
			                try {
			                    Files.deleteIfExists(Path.of(rutaFoto));
			                } catch (IOException e) {
			                    e.printStackTrace();
			                }
			                movimiento.setRutaFoto(null);
			                movimientoServicio.guardarMovimientos(movimiento);
			                getTableView().refresh();
			            }
			        });
			    }

			    @Override
			    protected void updateItem(String item, boolean empty) {
			        super.updateItem(item, empty);
			        if (empty) {
			            setGraphic(null);
			        } else {
			            Movimiento movimiento = getTableView().getItems().get(getIndex());
			            if (movimiento.getRutaFoto() == null || movimiento.getRutaFoto().isEmpty()) {
			                setGraphic(btnAgregar);
			                
			            } else {
			                setGraphic(hboxConFoto);
			                
			            }
			            
			            setAlignment(Pos.CENTER);
			        }
			    }
			});

	}

	private void listarMovimientos() {
		LOGGER.info("\nEjecutando estado de Movimientos");
		movimientoList.clear();
		movimientoList.addAll(movimientoServicio.listarMovientos(sessionService.getUsuarioLogueado()));
		tablaGastos.setItems(movimientoList);
	}

	private void recolectarDatosFormulario(Movimiento movimientos) {
		if (idMovimientoInterno != null) {
			movimientos.setId(idMovimientoInterno);
		}
		movimientos.setDescripcion(textDescripcion.getText());
		movimientos.setFecha(datePicker.getValue());
		try {
			String montoRegex = textMonto.getText();
			
			if(textMonto.getText().matches("^(\\d+\\.\\d+|\\d+|\\.\\d+)$")){
				movimientos.setMonto(Double.parseDouble(montoRegex));
			}else if(textMonto.getText().matches("^(\\d+\\,\\d+|\\d+|\\,\\d+)$")){
				String montoSinComa = montoRegex.replace(',', '.');
				movimientos.setMonto(Double.parseDouble(montoSinComa));
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			ControllerGeneralModel.alert(AlertType.WARNING, "Ingrese un numero", "El caracter no es un numero");
		}
		movimientos.setTipo(comboTipo.getValue());
		movimientos.setRutaFoto(fotoRutaSeleccionada);
		;

		Usuario user = sessionService.getUsuarioLogueado();

		if (user == null) {
			ControllerGeneralModel.alert(AlertType.ERROR, "Error", "No hay usuario logueado");
			return;
		} else {
			movimientos.setUsuarioLogeado(user.getIdUsuario());
			movimientos.setUsuario(user);
		}

	}

	@FXML
	public void onLimpiarFormulario() {
		LOGGER.info("se ejecuta boton limpiar");

		datePicker.setValue(null);
		textDescripcion.clear();
		textMonto.clear();
		comboTipo.getSelectionModel().clearSelection();
		fotoRutaSeleccionada = null;

	}

	@FXML
	public void onVolverMenuPrincipal(ActionEvent e) {
		LOGGER.info("se ejecuta boton MenuPrincipal");
		Object event = e.getSource();

		if (event.equals(btnMenuPrincipal)) {
			try {
				Scene scene = new Scene(fxmlLoader.load("mainMenu.fxml"));
				Stage stage = (Stage) btnMenuPrincipal.getScene().getWindow();
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.setTitle("Menú Principal");
				stage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	@FXML
	private void onSeleccionarImagen(ActionEvent event) {


		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
		File file = fileChooser.showOpenDialog(null);
		if (file == null) {
	        // El usuario cancela: no se hace nada
	        return;
	    }
		
		if (file != null) {
			try {
				// Guardar físicamente en la carpeta de la app
				String rutaGuardada = fotoService.guardarFoto(file, idMovimientoInterno);
				fotoRutaSeleccionada = rutaGuardada;

				ControllerGeneralModel.alert(AlertType.INFORMATION, "Imagen cargada",
						"Imagen guardada en: " + rutaGuardada);

			} catch (IOException e) {
				e.printStackTrace();
				ControllerGeneralModel.alert(AlertType.ERROR, "Error", "No se pudo copiar la imagen");
			}
		}
	}

	@FXML
	public void modificarMovimiento() {

		if (idMovimientoInterno == null) {
			ControllerGeneralModel.alert(AlertType.WARNING, "Informacion", "debe seleccionar una Movimiento");
			return;
		}

		boolean algunCampoVacio = datePicker.getValue() == null || textMonto.getText().isEmpty()
				|| comboTipo.getValue().isEmpty();

		if (algunCampoVacio) {
			ControllerGeneralModel.alert(AlertType.WARNING, "Informacion", "debe proporcionar algun Movimiento");
			datePicker.requestFocus();
			return;
		}

		Movimiento newMovimiento = new Movimiento();
		recolectarDatosFormulario(newMovimiento);
		movimientoServicio.guardarMovimientos(newMovimiento);
		ControllerGeneralModel.alert(AlertType.CONFIRMATION, "Informacion", "Movimiento Modificado");
		onLimpiarFormulario();
		listarMovimientos();

	}

	@FXML
	public void borrarMovimiento() {
		Movimiento movimiento = tablaGastos.getSelectionModel().getSelectedItem();
		if (movimiento == null) {
			ControllerGeneralModel.alert(AlertType.ERROR, "Error", "No ha seleccionado un Movimiento");
			return;
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que deseas Eliminar el Movimiento con el ID"+movimiento.getId()+"?\n", ButtonType.YES,
				ButtonType.NO);
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.YES) {
				LOGGER.info("Registro a Eliminar: " + movimiento.toString());
				
				String rutaFoto = movimiento.getRutaFoto();
	            if (rutaFoto != null && !rutaFoto.isEmpty()) {
	                try {
	                    Files.deleteIfExists(Path.of(rutaFoto)); // elimina archivo físico
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    ControllerGeneralModel.alert(AlertType.WARNING, "Advertencia",
	                            "No se pudo eliminar la imagen asociada: " + rutaFoto);
	                }
	            }
	            
				movimientoServicio.EliminarMovimientos(movimiento);
				ControllerGeneralModel.alert(AlertType.CONFIRMATION, "", "Movimiento Eliminado con ID " + movimiento.getId());
				onLimpiarFormulario();
				listarMovimientos();
			}
		});

		

	}

	private void cargarMovimientoEnFormulario(Movimiento movimiento) {
		idMovimientoInterno = movimiento.getId();
		datePicker.setValue(movimiento.getFecha());
		textDescripcion.setText(movimiento.getDescripcion());
		textMonto.setText(String.valueOf(movimiento.getMonto()));
		comboTipo.setValue(movimiento.getTipo());
		fotoRutaSeleccionada = movimiento.getRutaFoto();
	}

}
