/**
 * Agrupa los parametros de la regla de negocio que podrian cambiar en una version futura
 * (por ejemplo, si la autoridad electoral decidiera usar un radio distinto a 5 km).
 *
 * Este es el unico lugar del programa donde un setter tiene un proposito real: a
 * diferencia de Ciudadano o Colegio (que se cargan una vez desde el CSV y no cambian),
 * la distancia maxima es un parametro de configuracion que tiene sentido poder ajustar
 * sin modificar AsignadorVotacion ni recompilar esa clase.
 */
public class ConfiguracionAsignacion {

    private double distanciaMaximaKm;

    public ConfiguracionAsignacion() {
        this.distanciaMaximaKm = 5.0; // valor por defecto definido en el enunciado
    }

    public double getDistanciaMaximaKm() {
        return distanciaMaximaKm;
    }

    /**
     * Solo se acepta un valor positivo: una distancia maxima negativa o cero no tiene
     * sentido dentro de la regla de negocio, asi que se ignora en vez de dejar el
     * programa en un estado invalido.
     */
    public void setDistanciaMaximaKm(double distanciaMaximaKm) {
        if (distanciaMaximaKm > 0) {
            this.distanciaMaximaKm = distanciaMaximaKm;
        }
    }
}
