package com.example.crearticket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crearticket.adapters.TicketsAdapter;
import com.example.crearticket.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SastreDashboardActivity extends AppCompatActivity {

    private TextView tvCountPendiente, tvCountProceso, tvCountListo;
    private RecyclerView rvRecentTickets;
    private TicketsAdapter adapter;
    private List<Ticket> ticketList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sastre_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tvCountPendiente = findViewById(R.id.tvCountPendiente);
        tvCountProceso = findViewById(R.id.tvCountProceso);
        tvCountListo = findViewById(R.id.tvCountListo);
        rvRecentTickets = findViewById(R.id.rvRecentTickets);

        ticketList = new ArrayList<>();
        rvRecentTickets.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketsAdapter(ticketList, ticket -> {
            Intent intent = new Intent(this, DetallesTicketActivity.class);
            intent.putExtra("ticketId", ticket.getId());
            startActivity(intent);
        });
        rvRecentTickets.setAdapter(adapter);

        findViewById(R.id.btnQuickCreate).setOnClickListener(v -> {
            startActivity(new Intent(this, CrearTicketActivity.class));
        });

        findViewById(R.id.btnQuickConsult).setOnClickListener(v -> {
            startActivity(new Intent(this, ConsultarTicketsActivity.class));
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        loadDashboardData();
    }

    private void loadDashboardData() {
        // Cargar contadores
        db.collection("tickets").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int pendiente = 0;
            int proceso = 0;
            int listo = 0;

            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                String status = doc.getString("status");
                if (status != null) {
                    if (status.equalsIgnoreCase("Pendiente") || status.equalsIgnoreCase("Nuevo")) pendiente++;
                    else if (status.equalsIgnoreCase("En progreso") || status.equalsIgnoreCase("Falta material")) proceso++;
                    else if (status.equalsIgnoreCase("Terminado")) listo++;
                }
            }

            tvCountPendiente.setText(String.valueOf(pendiente));
            tvCountProceso.setText(String.valueOf(proceso));
            tvCountListo.setText(String.valueOf(listo));
        });

        // Cargar últimos tickets
        db.collection("tickets")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ticketList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Ticket ticket = doc.toObject(Ticket.class);
                        ticket.setId(doc.getId());
                        ticketList.add(ticket);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }
}