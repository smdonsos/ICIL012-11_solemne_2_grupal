/**
 * Representa el resultado de asignar (o intentar asignar) un ciudadano a un colegio.
 *
 * Los tres tipos posibles de asignacion se definen aqui como constantes en vez de
 * repetir el texto literal en AsignadorVotacion (donde se asignan) y en Main (donde se
 * comparan con .equals(...)): asi, un error de tipeo en cualquiera de los dos lugares lo
 * detecta el compilador (nombre de constante mal escrito) en vez de fallar en silencio
 * en tiempo de ejecucion, como pasaria comparando strings sueltos.
 */
public class Asignacion {

    public static final String NORMAL = "NORMAL";
    public static final String EXCEPCIONAL_POR_DISTANCIA = "EXCEPCIONAL POR DISTANCIA";
    public static final String SIN_ASIGNACION = "SIN ASIGNACION POR FALTA DE CUPOS";

    private Ciudadano ciudadano;
    private Colegio colegio;      // null si no hubo asignacion posible
    private double distanciaKm;   // distancia calculada con Haversine
    private String tipo;          // NORMAL, EXCEPCIONAL_POR_DISTANCIA o SIN_ASIGNACION

    public Asignacion(Ciudadano ciudadano, Colegio colegio,
                      double distanciaKm, String tipo) {
        this.ciudadano = ciudadano;
        this.colegio = colegio;
        this.distanciaKm = distanciaKm;
        this.tipo = tipo;
    }

    public Ciudadano getCiudadano() {
        return ciudadano;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public String getTipo() {
        return tipo;
    }

    // Convierte el objeto en un texto legible para imprimirlo directo
    @Override
    public String toString() {
        if (colegio == null) {
            return ciudadano.getNombre() + " -> SIN ASIGNAR [" + tipo + "]";
        }
        return ciudadano.getNombre() + " -> " + colegio.getNombre()
                + String.format(" (%.2f km) [%s]", distanciaKm, tipo);
    }
}