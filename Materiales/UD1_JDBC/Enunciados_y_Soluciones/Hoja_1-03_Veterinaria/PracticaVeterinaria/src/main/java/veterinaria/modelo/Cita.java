package veterinaria.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cita {

    private int id;
    private int idMascota;
    private String nombreMascota;     // no está en la tabla citas: se obtiene con un JOIN
    private int idVeterinario;
    private LocalDateTime fechaHora;
    private String tipo;              // CONSULTA, VACUNA, REVISION o CIRUGIA
    private String motivo;
    private BigDecimal precio;
    private String estado;            // PENDIENTE, REALIZADA o ANULADA

    public Cita(int id, int idMascota, String nombreMascota, int idVeterinario, LocalDateTime fechaHora,
                String tipo, String motivo, BigDecimal precio, String estado) {
        this.id = id;
        this.idMascota = idMascota;
        this.nombreMascota = nombreMascota;
        this.idVeterinario = idVeterinario;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.motivo = motivo;
        this.precio = precio;
        this.estado = estado;
    }

    public int getId()
    { return id; }
    
    public int getIdMascota() 
    { return idMascota; }
    
    public String getNombreMascota() {
        return nombreMascota; }
    
    public int getIdVeterinario() { 
        return idVeterinario; }
    
    public LocalDateTime getFechaHora() {
        return fechaHora; }
    
    public String getTipo() { 
        return tipo; }
    
    public String getMotivo() { 
        return motivo; }
    
    public BigDecimal getPrecio() { 
        return precio; }
    
    public String getEstado() { 
        return estado; }

    @Override
    public String toString() {
        return fechaHora.toLocalTime() + " " + nombreMascota + " - " + tipo
                + " (" + motivo + ") " + precio + " €";
    }
}
