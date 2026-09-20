import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Clase encargada de leer los archivos CSV y transformar cada linea de texto en objetos Ciudadano o Colegio.
 * Aqui se cumple el requisito obligatorio del enunciado: "el programa debe leer los datos desde los archivos CSV".
 *
 * Cada fila se valida antes de crear el objeto. Los campos obligatorios (los que efectivamente usa el resto
 * del programa: id/nombre/latitud/longitud en Ciudadano; codigo/nombre/latitud/longitud/capacidadMaxima en
 * Colegio) hacen descartar la fila si faltan o son invalidos. Los campos opcionales (rut, comuna) nunca se
 * consultan via getter en el resto del programa hoy, asi que si vienen vacios se completan con un valor por
 * defecto en vez de descartar la fila completa.
 */
public class LectorCSV {

    private static final int COLUMNAS_CIUDADANO = 6;
    private static final int COLUMNAS_COLEGIO = 6;
    private static final String VALOR_OPCIONAL_POR_DEFECTO = "SIN DATO";

    /**
     * Lee el archivo de ciudadanos y devuelve una lista de objetos Ciudadano, uno por cada fila valida del
     * CSV (sin contar el encabezado). Declara 'throws IOException' porque no maneja el error de archivo
     * aqui mismo (ruta inexistente, etc.), sino que lo deja subir hasta el try-catch de Main. Una fila
     * individual invalida, en cambio, NO aborta la lectura: se descarta, se informa por consola y se sigue
     * con la siguiente. Al terminar, se imprime un resumen con filas totales, correctas y el detalle de
     * fallos por razon.
     */
    public static ArrayList<Ciudadano> leerCiudadanos(String rutaArchivo) throws IOException {
        ArrayList<Ciudadano> ciudadanos = new ArrayList<Ciudadano>();

        // Contadores de fallo, uno por cada razon posible (mismo patron "contador" que ya usa
        // Main.mostrarResumen() para clasificar las asignaciones).
        int columnasIncorrectas = 0;
        int idVacio = 0;
        int nombreVacio = 0;
        int latitudInvalida = 0;
        int longitudInvalida = 0;

        // Se usa InputStreamReader con StandardCharsets.UTF_8
        // se lean bien sin importar la configuracion regional del computador donde se ejecute el programa.
        BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8));

        String linea;
        boolean primeraLinea = true; // bandera para saltar el encabezado
        int numeroLineaArchivo = 0;
        int filasTotales = 0;

        // Ciclo while: se lee el archivo linea por linea hasta llegar al final
        while ((linea = lector.readLine()) != null) {
            numeroLineaArchivo++;

            // La primera linea es el encabezado (id,rut,nombre,...), no un dato real
            if (primeraLinea) {
                primeraLinea = false;
                continue;
            }

            // Se ignoran lineas vacias, por si el archivo tiene un salto de linea de mas al final.
            // Las lineas en blanco no cuentan como fila de datos (no suman a filasTotales).
            if (linea.trim().isEmpty()) {
                continue;
            }

            filasTotales++;

            // Se separan las columnas por coma. El limite -1 evita que se pierda un campo vacio
            // al final de la linea (por defecto, split() descarta los campos vacios finales).
            String[] datos = linea.split(",", -1);

            if (datos.length != COLUMNAS_CIUDADANO) {
                columnasIncorrectas++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): se esperaban " + COLUMNAS_CIUDADANO
                        + " columnas y se encontraron " + datos.length
                        + " (revisar delimitador usado o campos faltantes/sobrantes)");
                continue;
            }

            // Se arma cada campo. trim() saca espacios (y el retorno de carro \r que dejan los archivos guardados en Windows).
            String id = datos[0].trim();
            if (id.isEmpty()) {
                idVacio++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'id' esta vacio");
                continue;
            }

            String rut = valorOpcional(datos[1]);

            String nombre = datos[2].trim();
            if (nombre.isEmpty()) {
                nombreVacio++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'nombre' esta vacio");
                continue;
            }

            String comuna = valorOpcional(datos[3]);

            // Conversion de texto a numero: String -> double, protegida con try/catch
            // (igual que Main ya protege la lectura completa con NumberFormatException).
            double latitud;
            try {
                latitud = Double.parseDouble(datos[4].trim());
            } catch (NumberFormatException e) {
                latitudInvalida++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'latitud' no es un numero valido ('"
                        + datos[4].trim() + "')");
                continue;
            }

            double longitud;
            try {
                longitud = Double.parseDouble(datos[5].trim());
            } catch (NumberFormatException e) {
                longitudInvalida++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'longitud' no es un numero valido ('"
                        + datos[5].trim() + "')");
                continue;
            }

            // Se crea el objeto Ciudadano con los datos ya convertidos y se agrega a la lista de resultados
            Ciudadano ciudadano = new Ciudadano(id, rut, nombre, comuna, latitud, longitud);
            ciudadanos.add(ciudadano);
        }

        lector.close(); // se libera el archivo una vez terminada la lectura

        imprimirResumenLectura(rutaArchivo, filasTotales, ciudadanos.size(), new int[] {
                columnasIncorrectas, idVacio, nombreVacio, latitudInvalida, longitudInvalida
        }, new String[] {
                "Numero de columnas incorrecto", "Campo 'id' vacio", "Campo 'nombre' vacio",
                "Campo 'latitud' invalido", "Campo 'longitud' invalido"
        });

        return ciudadanos;
    }

    /**
     * Lee el archivo de colegios y devuelve una lista de objetos Colegio.
     * Misma logica que leerCiudadanos(), pero con las columnas propias de un colegio (incluye capacidad
     * maxima al final).
     */
    public static ArrayList<Colegio> leerColegios(String rutaArchivo) throws IOException {
        ArrayList<Colegio> colegios = new ArrayList<Colegio>();

        int columnasIncorrectas = 0;
        int codigoVacio = 0;
        int nombreVacio = 0;
        int latitudInvalida = 0;
        int longitudInvalida = 0;
        int capacidadMaximaInvalida = 0;

        BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8));

        String linea;
        boolean primeraLinea = true;
        int numeroLineaArchivo = 0;
        int filasTotales = 0;

        while ((linea = lector.readLine()) != null) {
            numeroLineaArchivo++;

            if (primeraLinea) {
                primeraLinea = false;
                continue;
            }

            if (linea.trim().isEmpty()) {
                continue;
            }

            filasTotales++;

            String[] datos = linea.split(",", -1);

            if (datos.length != COLUMNAS_COLEGIO) {
                columnasIncorrectas++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): se esperaban " + COLUMNAS_COLEGIO
                        + " columnas y se encontraron " + datos.length
                        + " (revisar delimitador usado o campos faltantes/sobrantes)");
                continue;
            }

            String codigo = datos[0].trim();
            if (codigo.isEmpty()) {
                codigoVacio++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'codigo' esta vacio");
                continue;
            }

            String nombre = datos[1].trim();
            if (nombre.isEmpty()) {
                nombreVacio++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'nombre' esta vacio");
                continue;
            }

            String comuna = valorOpcional(datos[2]);

            double latitud;
            try {
                latitud = Double.parseDouble(datos[3].trim());
            } catch (NumberFormatException e) {
                latitudInvalida++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'latitud' no es un numero valido ('"
                        + datos[3].trim() + "')");
                continue;
            }

            double longitud;
            try {
                longitud = Double.parseDouble(datos[4].trim());
            } catch (NumberFormatException e) {
                longitudInvalida++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'longitud' no es un numero valido ('"
                        + datos[4].trim() + "')");
                continue;
            }

            // Conversion de texto a numero entero: String -> int
            int capacidadMaxima;
            try {
                capacidadMaxima = Integer.parseInt(datos[5].trim());
            } catch (NumberFormatException e) {
                capacidadMaximaInvalida++;
                System.out.println("Fila descartada (linea " + numeroLineaArchivo + " de "
                        + rutaArchivo + "): el campo obligatorio 'capacidadMaxima' no es un numero entero valido ('"
                        + datos[5].trim() + "')");
                continue;
            }

            Colegio colegio = new Colegio(codigo, nombre, comuna, latitud, longitud, capacidadMaxima);
            colegios.add(colegio);
        }

        lector.close();

        imprimirResumenLectura(rutaArchivo, filasTotales, colegios.size(), new int[] {
                columnasIncorrectas, codigoVacio, nombreVacio, latitudInvalida, longitudInvalida, capacidadMaximaInvalida
        }, new String[] {
                "Numero de columnas incorrecto", "Campo 'codigo' vacio", "Campo 'nombre' vacio",
                "Campo 'latitud' invalido", "Campo 'longitud' invalido", "Campo 'capacidadMaxima' invalido"
        });

        return colegios;
    }

    /**
     * Los campos opcionales (rut, comuna) no se descartan si vienen vacios: hoy ningun getter suyo se
     * consulta fuera de esta clase, asi que se completan con un valor por defecto en vez de perder toda
     * la fila por un dato que el programa no usa para calcular ni mostrar resultados.
     */
    private static String valorOpcional(String valorCrudo) {
        String valor = valorCrudo.trim();
        return valor.isEmpty() ? VALOR_OPCIONAL_POR_DEFECTO : valor;
    }

    /**
     * Imprime el mensaje final con el resultado completo de la lectura de un archivo: filas totales,
     * filas procesadas correctamente, filas no procesadas y el detalle por razon de fallo. Cada fila se
     * clasifica segun la PRIMERA razon de fallo detectada (el ciclo de lectura corta con 'continue' en
     * cuanto encuentra el primer campo invalido, asi que nunca se cuenta una fila mas de una vez).
     */
    private static void imprimirResumenLectura(String rutaArchivo, int filasTotales, int filasCorrectas,
                                                int[] cantidadesPorRazon, String[] etiquetasPorRazon) {
        int filasNoProcesadas = filasTotales - filasCorrectas;

        System.out.println("==================== RESUMEN DE LECTURA: " + rutaArchivo + " ====================");
        System.out.println("Filas totales (sin contar encabezado ni lineas en blanco): " + filasTotales);
        System.out.println("Filas procesadas correctamente:                            " + filasCorrectas);
        System.out.println("Filas no procesadas:                                       " + filasNoProcesadas);
        System.out.println("Detalle de filas no procesadas (primera razon de fallo detectada por fila):");

        for (int i = 0; i < etiquetasPorRazon.length; i++) {
            System.out.println("  - " + etiquetasPorRazon[i] + ": " + cantidadesPorRazon[i]);
        }

        System.out.println("=========================================================================================");
    }
}
