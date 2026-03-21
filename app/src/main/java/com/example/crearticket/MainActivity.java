package com.example.crearticket;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCliente, spinnerTipoEncargo, spinnerTipoPrenda;
    private EditText etDescripcion, etMedidas, etFechaEntrega, etNotas;
    private Button btnGuardar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        spinnerCliente = findViewById(R.id.spinnerCliente);
        spinnerTipoEncargo = findViewById(R.id.spinnerTipoEncargo);
        spinnerTipoPrenda = findViewById(R.id.spinnerTipoPrenda);
        etDescripcion = findViewById(R.id.etDescripcion);
        etMedidas = findViewById(R.id.etMedidas);
        etFechaEntrega = findViewById(R.id.etFechaEntrega);
        etNotas = findViewById(R.id.etNotas);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Configurar Spinner de Cliente
        String[] clientes = {getString(R.string.seleccionar_cliente), "Juan Pérez", "María García", "Carlos López", "Ana Martínez"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(adapter);

        // Selector de fecha
        etFechaEntrega.setOnClickListener(v -> showDatePicker());

        // Acción Guardar
        btnGuardar.setOnClickListener(v -> {
            if (validarCampos()) {
                new AlertDialog.Builder(this)
                        .setTitle("Éxito")
                        .setMessage("Ticket guardado correctamente.\nEstado inicial: Nuevo")
                        .setPositiveButton("Aceptar", null)
                        .show();
            }
        });

        // Acción Cancelar
        btnCancelar.setOnClickListener(v -> {
            limpiarCampos();
            Toast.makeText(this, "Campos limpiados", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validarCampos() {
        if (spinnerCliente.getSelectedItemPosition() == 0) {
            mostrarAlerta("Por favor, selecciona un cliente.");
            return false;
        }
        if (etDescripcion.getText().toString().trim().isEmpty()) {
            mostrarAlerta("La descripción es obligatoria.");
            return false;
        }
        if (etFechaEntrega.getText().toString().trim().isEmpty()) {
            mostrarAlerta("Debes seleccionar una fecha de entrega.");
            return false;
        }
        // Medidas y Notas son opcionales según el diseño
        return true;
    }

    private void mostrarAlerta(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage(mensaje)
                .setPositiveButton("Entendido", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void limpiarCampos() {
        spinnerCliente.setSelection(0);
        spinnerTipoEncargo.setSelection(0);
        spinnerTipoPrenda.setSelection(0);
        etDescripcion.setText("");
        etMedidas.setText("");
        etFechaEntrega.setText("");
        etNotas.setText("");
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, y, m, d) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", d, (m + 1), y);
                    etFechaEntrega.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
