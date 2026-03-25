package com.example.crearticket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crearticket.models.Client;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetallesClienteActivity extends AppCompatActivity {

    private TextView tvName, tvPhone, tvEmail, tvAddress;
    private MaterialButton btnEdit, btnDelete, btnCall;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private String clientId;
    private String currentPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cliente);

        db = FirebaseFirestore.getInstance();
        clientId = getIntent().getStringExtra("clientId");

        tvName = findViewById(R.id.tvClientDetailName);
        tvPhone = findViewById(R.id.tvClientDetailPhone);
        tvEmail = findViewById(R.id.tvClientDetailEmail);
        tvAddress = findViewById(R.id.tvClientDetailAddress);
        btnEdit = findViewById(R.id.btnEditClient);
        btnDelete = findViewById(R.id.btnDeleteClient);
        btnCall = findViewById(R.id.btnCallClient);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        if (clientId != null) {
            loadClientDetails();
        }

        btnDelete.setOnClickListener(v -> confirmDeletion());
        
        btnCall.setOnClickListener(v -> {
            if (currentPhoneNumber != null && !currentPhoneNumber.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + currentPhoneNumber));
                startActivity(intent);
            } else {
                Toast.makeText(this, "No hay un número de teléfono válido", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Funcionalidad de edición próximamente", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadClientDetails() {
        showLoading(true);
        db.collection("clients").document(clientId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    showLoading(false);
                    if (documentSnapshot.exists()) {
                        Client client = documentSnapshot.toObject(Client.class);
                        if (client != null) {
                            displayClient(client);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error al cargar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayClient(Client client) {
        tvName.setText(client.getName());
        tvPhone.setText(client.getPhone());
        currentPhoneNumber = client.getPhone();
        tvEmail.setText(client.getEmail() != null && !client.getEmail().isEmpty() ? client.getEmail() : "No registrado");
        tvAddress.setText(client.getAddress() != null && !client.getAddress().isEmpty() ? client.getAddress() : "Sin dirección");
    }

    private void confirmDeletion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Cliente")
                .setMessage("¿Estás seguro de que deseas eliminar este cliente?")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteClient())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteClient() {
        showLoading(true);
        db.collection("clients").document(clientId).delete()
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}