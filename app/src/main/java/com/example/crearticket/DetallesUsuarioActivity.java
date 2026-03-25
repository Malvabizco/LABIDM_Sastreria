package com.example.crearticket;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crearticket.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetallesUsuarioActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvRole, tvId;
    private MaterialButton btnDelete;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_usuario);

        db = FirebaseFirestore.getInstance();
        userId = getIntent().getStringExtra("userId");

        tvName = findViewById(R.id.tvDetailName);
        tvEmail = findViewById(R.id.tvDetailEmail);
        tvRole = findViewById(R.id.tvDetailRole);
        tvId = findViewById(R.id.tvDetailId);
        btnDelete = findViewById(R.id.btnDeleteUser);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        if (userId != null) {
            loadUserDetails();
        }

        btnDelete.setOnClickListener(v -> confirmDeletion());
    }

    private void loadUserDetails() {
        showLoading(true);
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    showLoading(false);
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            displayUser(user);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error al cargar detalles: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayUser(User user) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvRole.setText(user.getRole().toUpperCase());
        tvId.setText(user.getId());
    }

    private void confirmDeletion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Estás seguro de que deseas eliminar este usuario? Esta acción no se puede deshacer de Firestore.")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteUser())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteUser() {
        showLoading(true);
        db.collection("users").document(userId).delete()
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    Toast.makeText(this, "Usuario eliminado de la base de datos", Toast.LENGTH_SHORT).show();
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