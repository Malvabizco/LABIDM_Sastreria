package com.example.crearticket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crearticket.models.Client;
import com.example.crearticket.models.Ticket;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetallesTicketActivity extends AppCompatActivity {

    private TextView tvClient, tvType, tvDesc, tvMeasures, tvDate;
    private Spinner spinnerStatus;
    private MaterialButton btnUpdate, btnCall;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private String ticketId;
    private String clientPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ticket);

        db = FirebaseFirestore.getInstance();
        ticketId = getIntent().getStringExtra("ticketId");

        tvClient = findViewById(R.id.tvTicketDetailClient);
        tvType = findViewById(R.id.tvTicketDetailType);
        tvDesc = findViewById(R.id.tvTicketDetailDesc);
        tvMeasures = findViewById(R.id.tvTicketDetailMeasures);
        tvDate = findViewById(R.id.tvTicketDetailDate);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdate = findViewById(R.id.btnUpdateStatus);
        btnCall = findViewById(R.id.btnCallClientFromTicket);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        if (ticketId != null) {
            loadTicketDetails();
        }

        btnUpdate.setOnClickListener(v -> updateStatus());
        btnCall.setOnClickListener(v -> callClient());
    }

    private void loadTicketDetails() {
        showLoading(true);
        db.collection("tickets").document(ticketId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    showLoading(false);
                    if (documentSnapshot.exists()) {
                        Ticket ticket = documentSnapshot.toObject(Ticket.class);
                        if (ticket != null) {
                            displayTicket(ticket);
                            loadClientPhone(ticket.getClientId());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error al cargar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayTicket(Ticket ticket) {
        tvClient.setText(ticket.getClientName());
        tvType.setText(ticket.getTypeWork() + " - " + ticket.getTypeGarment());
        tvDesc.setText(ticket.getDescription());
        tvMeasures.setText(ticket.getMeasures().isEmpty() ? "Sin medidas" : ticket.getMeasures());
        
        if (ticket.getDeliveryDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvDate.setText(sdf.format(ticket.getDeliveryDate()));
        }

        // Set spinner to current status
        String currentStatus = ticket.getStatus();
        String[] statuses = getResources().getStringArray(R.array.estados_ticket_sastre);
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equalsIgnoreCase(currentStatus)) {
                spinnerStatus.setSelection(i);
                break;
            }
        }
    }

    private void loadClientPhone(String clientId) {
        db.collection("clients").document(clientId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        clientPhone = doc.getString("phone");
                    }
                });
    }

    private void updateStatus() {
        String newStatus = spinnerStatus.getSelectedItem().toString();
        showLoading(true);
        db.collection("tickets").document(ticketId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    Toast.makeText(this, "Estado actualizado a: " + newStatus, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void callClient() {
        if (clientPhone != null && !clientPhone.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + clientPhone));
            startActivity(intent);
        } else {
            Toast.makeText(this, "No hay teléfono registrado para este cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnUpdate.setEnabled(!loading);
    }
}