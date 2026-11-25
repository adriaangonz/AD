package veterinaria.modelo;

public class Veterinario {

    private int id;
    private String nombre;
    private String especialidad;
    private boolean activo;

    public Veterinario(int id, String nombre, String especialidad, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.activo = activo;
    }

    public int getId() { 
        return id; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    public String getEspecialidad() {
        return especialidad; 
    }
    public boolean isActivo() { 
        return activo; 
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + especialidad + ")";
    }
}
