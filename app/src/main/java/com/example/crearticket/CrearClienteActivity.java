package com.example.crearticket;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrearClienteActivity extends AppCompatActivity {

    EditText etNombre, etCorreo, etPassword, etConfirmPassword;
    Spinner spinnerRol, spinnerEstado;
    Button btnGuardar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        spinnerRol = findViewById(R.id.spinnerRol);
        spinnerEstado = findViewById(R.id.spinnerEstado);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        String[] roles = {"Administrador", "Sastre"};
        String[] estados = {"Activo", "Inactivo"};

        spinnerRol.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, roles));

        spinnerEstado.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, estados));

        btnGuardar.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();

            String rol = spinnerRol.getSelectedItem().toString();
            String estado = spinnerEstado.getSelectedItem().toString();

            if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this,
                    "Usuario creado:\n" +
                            nombre + "\n" +
                            correo + "\n" +
                            rol + " - " + estado,
                    Toast.LENGTH_LONG).show();

            etNombre.setText("");
            etCorreo.setText("");
            etPassword.setText("");
            etConfirmPassword.setText("");
            spinnerRol.setSelection(0);
            spinnerEstado.setSelection(0);
        });

        btnCancelar.setOnClickListener(v -> finish());
    }
}