package com.example.crearticket;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crearticket.models.Client;
import com.example.crearticket.models.Ticket;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrearTicketActivity extends AppCompatActivity {

    private Spinner spinnerClient, spinnerTypeWork, spinnerTypeGarment;
    private EditText etDescription, etMeasures, etDeliveryDate;
    private MaterialButton btnSave, btnCancel;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    
    private List<Client> clientList = new ArrayList<>();
    private List<String> clientNames = new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ticket);

        db = FirebaseFirestore.getInstance();

        spinnerClient = findViewById(R.id.spinnerClient);
        spinnerTypeWork = findViewById(R.id.spinnerTypeWork);
        spinnerTypeGarment = findViewById(R.id.spinnerTypeGarment);
        etDescription = findViewById(R.id.etDescription);
        etMeasures = findViewById(R.id.etMeasures);
        etDeliveryDate = findViewById(R.id.etDeliveryDate);
        btnSave = findViewById(R.id.btnSaveTicket);
        btnCancel = findViewById(R.id.btnCancelTicket);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        findViewById(R.id.btnAddClientQuick).setOnClickListener(v -> {
            startActivity(new Intent(this, CrearClienteActivity.class));
        });

        etDeliveryDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveTicket());

        loadClients();
    }

    private void loadClients() {
        db.collection("clients").get().addOnSuccessListener(queryDocumentSnapshots -> {
            clientList.clear();
            clientNames.clear();
            clientNames.add("Seleccionar cliente...");
            
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Client client = doc.toObject(Client.class);
                client.setId(doc.getId());
                clientList.add(client);
                clientNames.add(client.getName());
            }
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerClient.setAdapter(adapter);
        });
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedDate = calendar.getTime();
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            etDeliveryDate.setText(sdf.format(selectedDate));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveTicket() {
        int clientPos = spinnerClient.getSelectedItemPosition();
        if (clientPos <= 0) {
            Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = etDescription.getText().toString().trim();
        if (description.isEmpty()) {
            Toast.makeText(this, "La descripción es obligatoria", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        Client selectedClient = clientList.get(clientPos - 1);
        String id = db.collection("tickets").document().getId();
        
        Ticket ticket = new Ticket(
                id,
                selectedClient.getId(),
                selectedClient.getName(),
                spinnerTypeWork.getSelectedItem().toString(),
                spinnerTypeGarment.getSelectedItem().toString(),
                description,
                etMeasures.getText().toString().trim(),
                selectedDate,
                new Date(),
                "Nuevo",
                ""
        );

        db.collection("tickets").document(id).set(ticket)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    Toast.makeText(this, "Ticket creado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!loading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClients();
    }
}