package veterinaria.modelo;

import java.time.LocalDate;

public class Mascota {

    private int id;                       // 0 mientras no se haya guardado
    private String nombre;
    private String especie;               // perro, gato, conejo, ave u otro
    private LocalDate fechaNacimiento;    // puede ser null
    private int idCliente;

    public Mascota(int id, String nombre, String especie, LocalDate fechaNacimiento, int idCliente) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaNacimiento = fechaNacimiento;
        this.idCliente = idCliente;
    }

    /** Mascota nueva, todavía sin identificador. */
    public Mascota(String nombre, String especie, LocalDate fechaNacimiento, int idCliente) {
        this(0, nombre, especie, fechaNacimiento, idCliente);
    }

    public int getId() { 
        return id; 
    }
    
    public void setId(int id) {
        this.id = id; 
    }
    
    public String getNombre() {
        return nombre; 
    }
    
    public String getEspecie() {
        return especie; 
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento; 
    }
    
    public int getIdCliente() { 
        return idCliente; 
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente; 
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + especie + ", nacimiento: "
                + (fechaNacimiento == null ? "desconocido" : fechaNacimiento) + ")";
    }
}
