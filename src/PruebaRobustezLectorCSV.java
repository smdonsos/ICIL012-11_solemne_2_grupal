import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Test end-to-end de la robustez de LectorCSV. No forma parte del flujo oficial del
 * programa (Main.java sigue leyendo unicamente los CSV oficiales del enunciado,
 * datos/ciudadanos_santiago.csv y datos/colegios_santiago.csv): esta clase tiene su
 * propio main() y se ejecuta aparte, apuntando a los archivos de prueba en
 * datos/pruebas_robustez/ (generados por scripts/generar_fixtures_robustez.py).
 *
 * Cada caso llama a LectorCSV con un archivo de prueba, captura el resumen que
 * LectorCSV imprime por consola, y compara la cantidad de filas correctas y el
 * detalle por razon de fallo contra un resultado esperado conocido de antemano
 * (documentado tambien en el generador de fixtures).
 */
public class PruebaRobustezLectorCSV {

    private static final String CARPETA_FIXTURES = "datos/pruebas_robustez/";

    private static int casosTotales = 0;
    private static int casosFallidos = 0;

    public static void main(String[] args) throws IOException {

        // --- Casos sobre ciudadanos_santiago.csv ---
        verificarCiudadanos("ciudadanos_test_campos_obligatorios_vacios.csv", 100, 80,
                new String[] {"Numero de columnas incorrecto", "Campo 'id' vacio", "Campo 'nombre' vacio",
                        "Campo 'latitud' invalido", "Campo 'longitud' invalido"},
                new int[] {0, 10, 10, 0, 0});

        verificarCiudadanos("ciudadanos_test_valores_numericos_invalidos.csv", 100, 80,
                new String[] {"Numero de columnas incorrecto", "Campo 'id' vacio", "Campo 'nombre' vacio",
                        "Campo 'latitud' invalido", "Campo 'longitud' invalido"},
                new int[] {0, 0, 0, 10, 10});

        verificarCiudadanos("ciudadanos_test_columnas_incorrectas.csv", 100, 70,
                new String[] {"Numero de columnas incorrecto", "Campo 'id' vacio", "Campo 'nombre' vacio",
                        "Campo 'latitud' invalido", "Campo 'longitud' invalido"},
                new int[] {30, 0, 0, 0, 0});

        verificarCiudadanos("ciudadanos_test_campos_opcionales_vacios.csv", 100, 100,
                new String[] {"Numero de columnas incorrecto", "Campo 'id' vacio", "Campo 'nombre' vacio",
                        "Campo 'latitud' invalido", "Campo 'longitud' invalido"},
                new int[] {0, 0, 0, 0, 0});

        // --- Casos sobre colegios_santiago.csv ---
        verificarColegios("colegios_test_campos_obligatorios_vacios.csv", 100, 80,
                new String[] {"Numero de columnas incorrecto", "Campo 'codigo' vacio", "Campo 'nombre' vacio",
                        "Campo 'latitud' invalido", "Campo 'longitud' invalido", "Campo 'capacidadMaxima' invalido"},
                new int[] {0, 10, 10, 0, 0, 0});

        verificarColegios("colegios_test_valores_numericos_invalidos.csv", 100, 70,
                new String[] {"Numero de columnas incorrecto", "Campo 'codigo' vacio", "Campo 'nombre' vacio",
                        "Campo 'latitud' invalido", "Campo 'longitud' invalido", "Campo 'capacidadMaxima' invalido"},
                new int[] {0, 0, 0, 10, 10, 10});

        verificarColegios("colegios_test_columnas_incorrectas.csv", 100, 70,
                new String[] {"Numero de columnas incorrecto", "Campo 'codigo' vacio", "Campo 'nombre' vacio",
                        "Campo 'latitud' invalido", "Campo 'longitud' invalido", "Campo 'capacidadMaxima' invalido"},
                new int[] {30, 0, 0, 0, 0, 0});

        verificarColegios("colegios_test_campos_opcionales_vacios.csv", 100, 100,
                new String[] {"Numero de columnas incorrecto", "Campo 'codigo' vacio", "Campo 'nombre' vacio",
                        "Campo 'latitud' invalido", "Campo 'longitud' invalido", "Campo 'capacidadMaxima' invalido"},
                new int[] {0, 0, 0, 0, 0, 0});

        System.out.println();
        System.out.println("==================== RESULTADO DEL TEST E2E ====================");
        System.out.println("Casos totales:  " + casosTotales);
        System.out.println("Casos fallidos: " + casosFallidos);

        if (casosFallidos > 0) {
            System.out.println("RESULTADO: FALLO");
            System.exit(1);
        } else {
            System.out.println("RESULTADO: OK");
        }
    }

    /**
     * Lee un CSV de ciudadanos de prueba redirigiendo System.out a un buffer (para poder revisar
     * despues el resumen que LectorCSV imprime), y compara el resultado contra lo esperado.
     */
    private static void verificarCiudadanos(String nombreArchivo, int filasTotalesEsperadas,
                                             int filasCorrectasEsperadas, String[] etiquetasRazones,
                                             int[] cantidadesEsperadas) throws IOException {
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));

        ArrayList<Ciudadano> ciudadanos = LectorCSV.leerCiudadanos(CARPETA_FIXTURES + nombreArchivo);

        System.setOut(salidaOriginal);
        String salidaCapturada = buffer.toString();
        System.out.print(salidaCapturada); // se reimprime para dejar registro de la ejecucion en la consola real

        verificar(nombreArchivo, salidaCapturada, ciudadanos.size(), filasTotalesEsperadas,
                filasCorrectasEsperadas, etiquetasRazones, cantidadesEsperadas);
    }

    /**
     * Misma logica que verificarCiudadanos(), pero para un CSV de prueba de colegios.
     */
    private static void verificarColegios(String nombreArchivo, int filasTotalesEsperadas,
                                           int filasCorrectasEsperadas, String[] etiquetasRazones,
                                           int[] cantidadesEsperadas) throws IOException {
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));

        ArrayList<Colegio> colegios = LectorCSV.leerColegios(CARPETA_FIXTURES + nombreArchivo);

        System.setOut(salidaOriginal);
        String salidaCapturada = buffer.toString();
        System.out.print(salidaCapturada);

        verificar(nombreArchivo, salidaCapturada, colegios.size(), filasTotalesEsperadas,
                filasCorrectasEsperadas, etiquetasRazones, cantidadesEsperadas);
    }

    private static void verificar(String nombreArchivo, String salidaCapturada, int filasCorrectasReales,
                                   int filasTotalesEsperadas, int filasCorrectasEsperadas,
                                   String[] etiquetasRazones, int[] cantidadesEsperadas) {
        casosTotales++;
        StringBuilder diferencias = new StringBuilder();

        int filasTotalesReales = extraerNumero(salidaCapturada,
                "Filas totales (sin contar encabezado ni lineas en blanco): ");

        if (filasTotalesReales != filasTotalesEsperadas) {
            diferencias.append("filasTotales esperado=" + filasTotalesEsperadas + " real=" + filasTotalesReales + "; ");
        }

        if (filasCorrectasReales != filasCorrectasEsperadas) {
            diferencias.append("filasCorrectas esperado=" + filasCorrectasEsperadas + " real=" + filasCorrectasReales + "; ");
        }

        for (int i = 0; i < etiquetasRazones.length; i++) {
            int real = extraerNumero(salidaCapturada, "  - " + etiquetasRazones[i] + ": ");
            if (real != cantidadesEsperadas[i]) {
                diferencias.append(etiquetasRazones[i] + " esperado=" + cantidadesEsperadas[i] + " real=" + real + "; ");
            }
        }

        if (diferencias.length() > 0) {
            casosFallidos++;
            System.out.println("[FALLO] " + nombreArchivo + " -> " + diferencias);
        } else {
            System.out.println("[OK]    " + nombreArchivo);
        }
    }

    /**
     * Busca una etiqueta dentro del texto capturado y devuelve el numero que viene justo despues,
     * hasta el fin de esa linea. Se usa para leer los valores del resumen impreso por LectorCSV.
     */
    private static int extraerNumero(String texto, String etiqueta) {
        int posicionEtiqueta = texto.indexOf(etiqueta);
        int inicioNumero = posicionEtiqueta + etiqueta.length();
        int finLinea = texto.indexOf('\n', inicioNumero);
        String numeroTexto = texto.substring(inicioNumero, finLinea).trim();
        return Integer.parseInt(numeroTexto);
    }
}
