import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase principal del programa.
 * Se encarga de coordinar todo el proceso: 
 * Leer los datos desde los CSV,
 * Ejecutar la asignacion de ciudadanos a colegios, y mostrar los resultados por consola. No contiene logica de negocio propia.
 */
public class Main {

    public static void main(String[] args) {

        // Rutas de los archivos CSV.
        String rutaCiudadanos = "datos/ciudadanos_santiago.csv";
        String rutaColegios = "datos/colegios_santiago.csv";

        // Todo el flujo va dentro de un try porque leer archivos puede fallar
        // (que no exista el archivo, que un dato numerico este mal escrito, etc.)
        try {

            // 1) Lectura de los CSV: se transforma cada linea de texto en objetos Ciudadano y Colegio.
            // Cada lectura imprime su propio resumen (filas totales, correctas y detalle de fallos).
            ArrayList<Ciudadano> ciudadanos = LectorCSV.leerCiudadanos(rutaCiudadanos);
            ArrayList<Colegio> colegios = LectorCSV.leerColegios(rutaColegios);

            // 2) Se delega toda la logica de asignacion (distancia, cupos, reglas de negocio) a la clase AsignadorVotacion.
            // La distancia maxima (5 km) queda en ConfiguracionAsignacion en vez de una constante fija,
            // para poder ajustarla a futuro sin tocar AsignadorVotacion.
            ConfiguracionAsignacion configuracion = new ConfiguracionAsignacion();
            AsignadorVotacion asignador = new AsignadorVotacion(configuracion);
            ArrayList<Asignacion> asignaciones = asignador.asignarCiudadanos(ciudadanos, colegios);

            // 3) Se muestran los resultados en dos vistas: detalle y resumen.
            mostrarResultados(asignaciones);
            mostrarResumen(asignaciones, colegios);

        } catch (IOException e) {
            // Se lanza si el archivo no existe o no se puede abrir (ej. ruta incorrecta, archivo movido, permisos).
            System.out.println("Error al leer los archivos CSV: " + e.getMessage());

        } catch (NumberFormatException e) {
            // Se lanza si una columna que deberia ser numero (latitud, longitud, capacidad) viene con texto invalido en el CSV.
            System.out.println("Error al convertir un dato numerico del CSV: " + e.getMessage());

        } catch (Exception e) {
            // Red de seguridad: cualquier otro error no previsto no bota el programa de forma abrupta, sino que muestra un mensaje.
            System.out.println("Ocurrio un error inesperado: " + e.getMessage());
        }
    }

    /**
     * Imprime el detalle de cada asignacion: que ciudadano quedo en que
     * colegio, a que distancia, y bajo que tipo de regla (normal,
     * excepcional o sin asignar).
     */
    private static void mostrarResultados(ArrayList<Asignacion> asignaciones) {
        System.out.println("============================================================");
        System.out.println("      ASIGNACION DE CIUDADANOS A LOCALES DE VOTACION");
        System.out.println("============================================================");

        // Recorrido tipo for-each: uno de los ciclos que pide el enunciado
        for (Asignacion asignacion : asignaciones) {
            Ciudadano ciudadano = asignacion.getCiudadano();

            System.out.println("Ciudadano: " + ciudadano.getId() + " - " + ciudadano.getNombre());

            // Condicional: si el colegio es null significa que ese ciudadano no pudo ser asignado a ningun local (sin cupos disponibles)
            
            if (asignacion.getColegio() != null) {
                System.out.println("Local:     " + asignacion.getColegio().getCodigo()
                        + " - " + asignacion.getColegio().getNombre());
                System.out.printf("Distancia: %.2f km%n", asignacion.getDistanciaKm());
            } else {
                System.out.println("Local:     SIN LOCAL DISPONIBLE");
            }

            System.out.println("Tipo:      " + asignacion.getTipo());
            System.out.println("------------------------------------------------------------");
        }
    }

    // Calcula y muestra un resumen general: cuantas asignaciones fueron normales, cuantas excepcionales, cuantos ciudadanos quedaron sin asignar, y cuantos cupos ocupo cada colegio.

    private static void mostrarResumen(ArrayList<Asignacion> asignaciones,
                                       ArrayList<Colegio> colegios) {

        // Variables acumuladoras, se inician en 0 y se van sumando a medida que se recorren las asignaciones (patron "contador")
        int normales = 0;
        int excepcionales = 0;
        int sinAsignacion = 0;

        for (Asignacion asignacion : asignaciones) {
            // Cadena de condicionales para clasificar cada asignacion segun el texto guardado en el atributo "tipo"
            if (asignacion.getTipo().equals(Asignacion.NORMAL)) {
                normales++;
            } else if (asignacion.getTipo().equals(Asignacion.EXCEPCIONAL_POR_DISTANCIA)) {
                excepcionales++;
            } else {
                sinAsignacion++;
            }
        }

        System.out.println();
        System.out.println("====================== RESUMEN FINAL ======================");
        System.out.println("Total ciudadanos:              " + asignaciones.size());
        System.out.println("Asignaciones normales:         " + normales);
        System.out.println("Asignaciones excepcionales:    " + excepcionales);
        System.out.println("Ciudadanos sin asignacion:     " + sinAsignacion);
        System.out.println();
        System.out.println("Ocupacion por colegio:");

        // Se recorre la lista de colegios para mostrar cuantos cupos ocupo cada uno versus su capacidad maxima
        for (Colegio colegio : colegios) {
            System.out.println(colegio.getCodigo() + " - " + colegio.getNombre()
                    + ": " + colegio.getCantidadAsignados()
                    + "/" + colegio.getCapacidadMaxima());
        }
    }
}