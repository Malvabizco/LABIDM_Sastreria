package Clientes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.crearticket.R; // Asegúrate de que este paquete coincida con el tuyo

public class CrearClienteActivity extends AppCompatActivity {

    // Declarar los elementos del diseño [cite: 296, 297, 302, 303]
    private EditText etNombre, etTelefono, etDireccion, etReferencia, etNotas;
    private Button btnGuardar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);

        // Vincular con el XML
        etNombre = findViewById(R.id.etNombreCompleto);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        etReferencia = findViewById(R.id.etReferencia);
        etNotas = findViewById(R.id.etNotas);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Configurar el botón Guardar [cite: 302]
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCliente();
            }
        });

        // Configurar el botón Cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la pantalla actual
            }
        });
    }

    private void guardarCliente() {
        // Obtener los datos de los campos
        String nombre = etNombre.getText().toString();
        String telefono = etTelefono.getText().toString();

        // Validar campos obligatorios
        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Nombre y Teléfono son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Cliente usando tu clase [cite: 293]
        Cliente nuevoCliente = new Cliente(nombre, telefono);
        nuevoCliente.setDireccion(etDireccion.getText().toString()); // [cite: 298]
        nuevoCliente.setReferencia(etReferencia.getText().toString()); // [cite: 299]
        nuevoCliente.setNotas(etNotas.getText().toString()); // [cite: 300]

        // Aquí podrías enviar 'nuevoCliente' a una base de datos [cite: 294]
        Toast.makeText(this, "Cliente " + nombre + " guardado", Toast.LENGTH_LONG).show();
        finish();
    }
}