package veterinaria.modelo;

public class Cliente {

    private int id;            // 0 mientras no se haya guardado en la base de datos
    private String dni;
    private String nombre;
    private String telefono;
    private String email;

    public Cliente(int id, String dni, String nombre, String telefono, String email) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    /** Cliente nuevo, todavía sin identificador. */
    public Cliente(String dni, String nombre, String telefono, String email) {
        this(0, dni, nombre, telefono, email);
    }

    public int getId() {
        return id; 
    }
    
    public void setId(int id) {
        this.id = id; 
    }
    
    public String getDni() { 
        return dni; 
    }
    public String getNombre() {
        return nombre; 
    }
    public String getTelefono() {
        return telefono; 
    }
    public String getEmail() { 
        return email; 
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + dni + ")";
    }
}
