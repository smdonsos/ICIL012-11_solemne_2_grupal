/**
 * Representa a un ciudadano que debe ser asignado a un local de votacion.
 * Los datos de esta clase se cargan desde el archivo. El objeto se construye una sola vez, con
 * todos sus datos, y no cambia despues: no tiene setters porque ningun punto del programa necesita
 * modificar un Ciudadano ya creado.
 */
public class Ciudadano {

    // Atributos privados: encapsulamiento. Nadie fuera de la clase puede modificar estos valores directamente.
    private String id;
    private String rut;
    private String nombre;
    private String comuna;
    private double latitud;
    private double longitud;

    
    // * Constructor: crea un Ciudadano completo de una vez, normalmente llamado desde LectorCSV al procesar cada linea del archivo.
    public Ciudadano(String id, String rut, String nombre, String comuna,
                     double latitud, double longitud) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.comuna = comuna;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters: permiten leer cada atributo desde otras clases
    public String getId() {
        return id;
    }

    public String getRut() {
        return rut;
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
}