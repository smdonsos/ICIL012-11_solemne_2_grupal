/**
 * Clase auxiliar que empareja un Colegio con la distancia calculada hacia un ciudadano en particular. 
 * Se usa dentro de AsignadorVotacion: para cada ciudadano se genera una lista de ColegioDistancia (uno por cada colegio existente), y esa se ordena de menor a mayor distancia con Bubble Sort.
 */
public class ColegioDistancia {

    private Colegio colegio;
    private double distancia; // distancia en km hacia un ciudadano especifico

    /**
     * Constructor: junta un colegio con su distancia ya calculada.
     * El calculo real de Haversine ocurre antes, en AsignadorVotacion,
     */
    public ColegioDistancia(Colegio colegio, double distancia) {
        this.colegio = colegio;
        this.distancia = distancia;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public double getDistancia() {
        return distancia;
    }
}