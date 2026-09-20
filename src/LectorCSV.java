import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Clase encargada de leer los archivos CSV y transformar cada linea de texto en objetos Ciudadano o Colegio
 * Aqui se cumple el requisito obligatorio del enunciado: "el programa debe leer los datos desde los archivos CSV"
 */
public class LectorCSV {

    /**
     * Lee el archivo de ciudadanos y devuelve una lista de objetos Ciudadano, uno por cada fila del CSV (sin contar el encabezado).
     * Declara 'throws IOException' porque no maneja el error aqui mismo, sino que lo deja subir hasta el try-catch de Main.
     */
    public static ArrayList<Ciudadano> leerCiudadanos(String rutaArchivo) throws IOException {
        ArrayList<Ciudadano> ciudadanos = new ArrayList<Ciudadano>();

        // Se usa InputStreamReader con StandardCharsets.UTF_8 
        // se lean bien sin importar la configuracion regional del computador donde se ejecute el programa.
        BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8));

        String linea;
        boolean primeraLinea = true; // bandera para saltar el encabezado

        // Ciclo while: se lee el archivo linea por linea hasta llegar al final
        while ((linea = lector.readLine()) != null) {

            // La primera linea es el encabezado (id,rut,nombre,...), no un dato real
            if (primeraLinea) {
                primeraLinea = false;
                continue;
            }

            // Se ignoran lineas vacias, por si el archivo tiene un salto de linea de mas al final
            if (linea.trim().isEmpty()) {
                continue;
            }

            // Se separan las columnas por coma
            String[] datos = linea.split(",");

            // Se arma cada campo. trim() saca espacios (y el retorno de carro \r que dejan los archivos guardados en Windows).
            String id = datos[0].trim();
            String rut = datos[1].trim();
            String nombre = datos[2].trim();
            String comuna = datos[3].trim();

            // Conversion de texto a numero: String -> double
            double latitud = Double.parseDouble(datos[4].trim());
            double longitud = Double.parseDouble(datos[5].trim());

            // Se crea el objeto Ciudadano con los datos ya convertidos y se agrega a la lista de resultados
            Ciudadano ciudadano = new Ciudadano(id, rut, nombre, comuna, latitud, longitud);
            ciudadanos.add(ciudadano);
        }

        lector.close(); // se libera el archivo una vez terminada la lectura
        return ciudadanos;
    }

    /**
     * Lee el archivo de colegios y devuelve una lista de objetos Colegio.
     * Misma logica que leerCiudadanos(), pero con las columnas propias de un colegio (incluye capacidad maxima al final).
     */
    public static ArrayList<Colegio> leerColegios(String rutaArchivo) throws IOException {
        ArrayList<Colegio> colegios = new ArrayList<Colegio>();

        BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8));

        String linea;
        boolean primeraLinea = true;

        while ((linea = lector.readLine()) != null) {
            if (primeraLinea) {
                primeraLinea = false;
                continue;
            }

            if (linea.trim().isEmpty()) {
                continue;
            }

            String[] datos = linea.split(",");

            String codigo = datos[0].trim();
            String nombre = datos[1].trim();
            String comuna = datos[2].trim();
            double latitud = Double.parseDouble(datos[3].trim());
            double longitud = Double.parseDouble(datos[4].trim());

            // Conversion de texto a numero entero: String -> int
            int capacidadMaxima = Integer.parseInt(datos[5].trim());

            Colegio colegio = new Colegio(codigo, nombre, comuna,
                    latitud, longitud, capacidadMaxima);
            colegios.add(colegio);
        }

        lector.close();
        return colegios;
    }
}