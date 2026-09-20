/**
 * Clase encargada exclusivamente de calcular distancias geograficas.
 * Implementa la formula de Haversine, 
 */
public class CalculadoraDistancia {

    // Radio promedio de la Tierra en kilometros. Es la constante 'R' de la formula de Haversine.
    private static final double RADIO_TIERRA_KM = 6371.0;

     // * Calcula la distancia en kilometros entre dos coordenadas geograficas dadas en grados decimales 
     
    public static double calcularHaversine(double latitud1, double longitud1,
                                            double latitud2, double longitud2) {

        // Paso 1: convertir todo a radianes
        double lat1Rad = Math.toRadians(latitud1);
        double lat2Rad = Math.toRadians(latitud2);
        double diferenciaLat = Math.toRadians(latitud2 - latitud1);
        double diferenciaLon = Math.toRadians(longitud2 - longitud1);

        // Paso 2: aplicar la parte 'a' de la formula de Haversine..
        double a = Math.sin(diferenciaLat / 2) * Math.sin(diferenciaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(diferenciaLon / 2) * Math.sin(diferenciaLon / 2);

        // Paso 3: convertir 'a' en un angulo central 'c' usando atan2,
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Paso 4: multiplicar el angulo por el radio terrestre para obtener la distancia real en kilometros (arco = radio x angulo).
        return RADIO_TIERRA_KM * c;
    }
}