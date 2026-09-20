/**
 * Representa un local de votacion (colegio publico), con su ubicacion geografica y el control de cuantos ciudadanos tiene asignados hastam el momento. Los datos se cargan desde colegios_santiago.csv.
 * El unico dato que cambia despues de creado el objeto es cantidadAsignados, y solo a traves de
 * agregarCiudadano(): no tiene setters generales porque nada mas en el programa necesita modificar
 * un Colegio ya creado.
 */
public class Colegio {

    private String codigo;
    private String nombre;
    private String comuna;
    private double latitud;
    private double longitud;
    private int capacidadMaxima;
    private int cantidadAsignados; // cuantos ciudadanos ya ocuparon un cupo aqui

    // * Constructor: crea el colegio con su capacidad maxima definida, y arranca con 0 ciudadanos asignados (nadie ha llegado todavia).
    
    public Colegio(String codigo, String nombre, String comuna,
                   double latitud, double longitud, int capacidadMaxima) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.comuna = comuna;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidadMaxima = capacidadMaxima;
        this.cantidadAsignados = 0;
    }


    // * Indica si el colegio todavia tiene espacio para un ciudadano mas.
    public boolean tieneCupo() {
        return cantidadAsignados < capacidadMaxima;
    }

     // * Ocupa un cupo del colegio, sumando 1 al contador.
    
    public void agregarCiudadano() {
        if (tieneCupo()) {
            cantidadAsignados++;
        }
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getComuna() {
        return comuna;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public int getCantidadAsignados() {
        return cantidadAsignados;
    }
}