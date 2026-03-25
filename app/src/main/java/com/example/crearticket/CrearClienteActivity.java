package com.example.crearticket;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crearticket.models.Client;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class CrearClienteActivity extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etAddress;
    private MaterialButton btnSave;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);

        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etClientName);
        etPhone = findViewById(R.id.etClientPhone);
        etEmail = findViewById(R.id.etClientEmail);
        etAddress = findViewById(R.id.etClientAddress);
        btnSave = findViewById(R.id.btnSaveClient);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveClient());
    }

    private void saveClient() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Nombre y Teléfono son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        String id = db.collection("clients").document().getId();
        Client client = new Client(id, name, phone, email, address);

        db.collection("clients").document(id).set(client)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    Toast.makeText(this, "Cliente guardado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!loading);
    }
}