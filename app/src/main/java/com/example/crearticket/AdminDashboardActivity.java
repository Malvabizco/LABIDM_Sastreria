package com.example.crearticket;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView cardUsers, cardClients, cardTickets, cardReports;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();

        // Initialize Cards
        cardUsers = findViewById(R.id.cardUsers);
        cardClients = findViewById(R.id.cardClients);
        cardTickets = findViewById(R.id.cardTickets);
        cardReports = findViewById(R.id.cardReports);

        // Click Listeners
        cardUsers.setOnClickListener(v -> {
            startActivity(new Intent(this, ConsultarUsuariosActivity.class));
        });

        cardClients.setOnClickListener(v -> {
            startActivity(new Intent(this, ClientesActivity.class));
        });

        cardTickets.setOnClickListener(v -> {
            // Reutilizar o crear para Admin
            // startActivity(new Intent(this, ConsultarTicketsActivity.class));
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}