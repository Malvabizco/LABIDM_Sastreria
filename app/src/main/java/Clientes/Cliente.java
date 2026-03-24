package Clientes;

public class Cliente {
    private String nombreCompleto; // Obligatorio [cite: 296, 312]
    private String telefono;       // Obligatorio [cite: 297, 313]
    private String direccion;      // Opcional [cite: 298, 314]
    private String referencia;     // Opcional [cite: 299, 315]
    private String notas;          // Opcional [cite: 300, 316]
    private String fechaRegistro;  // Para el módulo de detalles [cite: 317]

    // Constructor vacío (necesario para algunas bases de datos)
    public Cliente() {}

    // Constructor con los campos principales
    public Cliente(String nombreCompleto, String telefono) {
        this.nombreCompleto = nombreCompleto; // [cite: 296]
        this.telefono = telefono;             // [cite: 297]
    }

    // Getters y Setters (Para que otras pantallas puedan leer y escribir los datos)
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}