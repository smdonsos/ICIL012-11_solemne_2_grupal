/**
 * Representa el resultado de asignar (o intentar asignar) un ciudadano a un colegio.
 */
public class Asignacion {

    private Ciudadano ciudadano;
    private Colegio colegio;      // null si no hubo asignacion posible
    private double distanciaKm;   // distancia calculada con Haversine
    private String tipo;          // "NORMAL", "EXCEPCIONAL POR DISTANCIA" o similar

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