package com.example.crearticket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton; // Import necesario para el botón de regreso
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Import necesario para el botón +

public class AdminTickets extends AppCompatActivity {

    private ImageButton btnBack;
    private FloatingActionButton fabAddTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de que el nombre del XML sea exactamente este
        setContentView(R.layout.activity_admin_tickets);

        // 1. Inicializar componentes
        btnBack = findViewById(R.id.btnBackTickets);

        // 2. Configurar botón de regreso
        // Al presionarlo, cerramos esta actividad para volver al AdminDashboard
        btnBack.setOnClickListener(v -> {
            finish();
        });

        // Nota: Como estamos validando el diseño con datos estáticos en el XML,
        // no es necesario inicializar adaptadores o listas de Firebase por ahora.
    }
}