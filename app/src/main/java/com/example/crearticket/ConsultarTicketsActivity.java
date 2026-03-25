package com.example.crearticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crearticket.adapters.TicketsAdapter;
import com.example.crearticket.models.Ticket;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConsultarTicketsActivity extends AppCompatActivity {

    private RecyclerView rvAllTickets;
    private TicketsAdapter adapter;
    private List<Ticket> ticketList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_tickets);

        db = FirebaseFirestore.getInstance();
        rvAllTickets = findViewById(R.id.rvAllTickets);
        progressBar = findViewById(R.id.progressBar);
        fabAddTicket = findViewById(R.id.fabAddTicket);

        ticketList = new ArrayList<>();
        rvAllTickets.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketsAdapter(ticketList, ticket -> {
            Intent intent = new Intent(this, DetallesTicketActivity.class);
            intent.putExtra("ticketId", ticket.getId());
            startActivity(intent);
        });
        rvAllTickets.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        fabAddTicket.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearTicketActivity.class));
        });

        loadAllTickets();
    }

    private void loadAllTickets() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("tickets")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ticketList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Ticket ticket = doc.toObject(Ticket.class);
                        ticket.setId(doc.getId());
                        ticketList.add(ticket);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error al cargar tickets: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllTickets();
    }
}