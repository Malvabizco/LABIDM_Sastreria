package com.example.crearticket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    private MaterialButton btnCrear, btnClientes, btnCerrarSesion;
    private TextView tvPendientes, tvProceso, tvCompletados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnCrear = findViewById(R.id.btnCrear);
        btnClientes = findViewById(R.id.btnClientes);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        tvPendientes = findViewById(R.id.tvPendientes);
        tvProceso = findViewById(R.id.tvProceso);
        tvCompletados = findViewById(R.id.tvCompletados);

        // Datos iniciales
        tvPendientes.setText("Pendientes\n0");
        tvProceso.setText("Proceso\n0");
        tvCompletados.setText("Listos\n0");


        btnCrear.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });


        btnClientes.setOnClickListener(v -> {
            startActivity(new Intent(this, ClientesActivity.class));
        });


        btnCerrarSesion.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}