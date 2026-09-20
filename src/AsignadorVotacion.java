import java.util.ArrayList;


 // * Aqui se aplican las reglas de negocio del enunciado: distancia maxima, cupos disponibles, y el orden de prioridad entre asignacion normal, excepcional, o sin asignar.
 //
 // A diferencia de LectorCSV y CalculadoraDistancia (utilidades sin estado, por eso son
 // estaticas), AsignadorVotacion SI tiene estado propio: la ConfiguracionAsignacion que
 // recibe en el constructor. Por eso se instancia con 'new' en vez de usar metodos
 // estaticos.

public class AsignadorVotacion {

    private final ConfiguracionAsignacion configuracion;

    public AsignadorVotacion(ConfiguracionAsignacion configuracion) {
        this.configuracion = configuracion;
    }

    // * Metodo principal: recorre todos los ciudadanos y le busca colegio a cada uno, devolviendo la lista completa de asignaciones

    public ArrayList<Asignacion> asignarCiudadanos(ArrayList<Ciudadano> ciudadanos,
                                                    ArrayList<Colegio> colegios) {
        ArrayList<Asignacion> asignaciones = new ArrayList<Asignacion>();

        // Ciclo for-each: se procesa un ciudadano a la vez
        for (Ciudadano ciudadano : ciudadanos) {

            // Paso 1: calcular la distancia de este ciudadano hacia
            ArrayList<ColegioDistancia> alternativas = calcularAlternativas(ciudadano, colegios);

            // Paso 2: ordenar esas alternativas de mas cercana a mas lejana,
            ordenarPorDistancia(alternativas);

            // Paso 3: intentar la asignacion NORMAL (colegio con cupo Y a <= 5 km). 
            ColegioDistancia seleccion = buscarAsignacionNormal(alternativas);
            String tipo = Asignacion.NORMAL;

            // Paso 4: si no hubo ninguna opcion normal, se relaja la regla de distancia y se busca solo por cupo (asignacion excepcional)
            if (seleccion == null) {
                seleccion = buscarAsignacionExcepcional(alternativas);
                tipo = Asignacion.EXCEPCIONAL_POR_DISTANCIA;
            }

            // Paso 5: segun si se encontro colegio o no, se arma la Asignacion final para este ciudadano
            if (seleccion != null) {
                // Se ocupa el cupo del colegio ganador para que los siguientes ciudadanos ya no puedan usar ese lugar
                seleccion.getColegio().agregarCiudadano();

                Asignacion asignacion = new Asignacion(
                        ciudadano,
                        seleccion.getColegio(),
                        seleccion.getDistancia(),
                        tipo
                );
                asignaciones.add(asignacion);
            } else {
                // Ningun colegio tenia cupo disponible: el ciudadano queda registrado igual, pero sin colegio (null)
                Asignacion asignacion = new Asignacion(
                        ciudadano,
                        null,
                        -1,
                        Asignacion.SIN_ASIGNACION
                );
                asignaciones.add(asignacion);
            }
        }

        return asignaciones;
    }

 
     // * Calcula la distancia entre un ciudadano y cada uno de los colegios, y devuelve una lista de pares (colegio, distancia) sin filtrar ni ordenar todavia.
     
    private ArrayList<ColegioDistancia> calcularAlternativas(Ciudadano ciudadano,
                                                              ArrayList<Colegio> colegios) {
        ArrayList<ColegioDistancia> alternativas = new ArrayList<ColegioDistancia>();

        for (Colegio colegio : colegios) {
            // Se delega el calculo geografico a CalculadoraDistancia,
            // que aplica la formula de Haversine (separacion de responsabilidades)
            double distancia = CalculadoraDistancia.calcularHaversine(
                    ciudadano.getLatitud(),
                    ciudadano.getLongitud(),
                    colegio.getLatitud(),
                    colegio.getLongitud()
            );

            alternativas.add(new ColegioDistancia(colegio, distancia));
        }

        return alternativas;
    }


     // * Ordena la lista de alternativas de menor a mayor distancia usando el algoritmo Bubble Sort

    private void ordenarPorDistancia(ArrayList<ColegioDistancia> alternativas) {
        for (int i = 0; i < alternativas.size() - 1; i++) {
            for (int j = 0; j < alternativas.size() - 1 - i; j++) {
                if (alternativas.get(j).getDistancia() > alternativas.get(j + 1).getDistancia()) {
                    // Intercambio clasico usando una variable temporal
                    ColegioDistancia temporal = alternativas.get(j);
                    alternativas.set(j, alternativas.get(j + 1));
                    alternativas.set(j + 1, temporal);
                }
            }
        }
    }


     // * Busca la primera alternativa (ya ordenada por distancia) que cumpla
     // * las dos condiciones de una asignacion normal: estar a 5 km o menos, y que el colegio todavia tenga cupo disponible.

    private ColegioDistancia buscarAsignacionNormal(ArrayList<ColegioDistancia> alternativas) {
        for (ColegioDistancia alternativa : alternativas) {
            if (alternativa.getDistancia() <= configuracion.getDistanciaMaximaKm()
                    && alternativa.getColegio().tieneCupo()) {
                return alternativa;
            }
        }

        return null;
    }

     // * Busca la primera alternativa con cupo disponible.
     
    private ColegioDistancia buscarAsignacionExcepcional(ArrayList<ColegioDistancia> alternativas) {
        for (ColegioDistancia alternativa : alternativas) {
            if (alternativa.getColegio().tieneCupo()) {
                return alternativa;
            }
        }

        return null;
    }
}