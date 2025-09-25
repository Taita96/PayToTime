package gm.PaynTime.servicio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
//import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FotoService {

    @Value("${app.fotos.directorio}")
    private String directorioRaiz;

    public String guardarFoto(File archivo, Integer idMovimiento) throws IOException {
        // Crear carpeta si no existe
        Path carpeta = Paths.get(directorioRaiz);
        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }

        // Nombre único
        String nombreArchivo = "movimiento_" + idMovimiento + "_" + archivo.getName();

        Path rutaArchivo = carpeta.resolve(nombreArchivo);

        // Copiar archivo
        Files.copy(archivo.toPath(), rutaArchivo);

        return rutaArchivo.toString();
    }

}
