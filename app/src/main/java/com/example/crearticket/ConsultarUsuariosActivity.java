package com.example.crearticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crearticket.adapters.UsuariosAdapter;
import com.example.crearticket.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConsultarUsuariosActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private UsuariosAdapter adapter;
    private List<User> userList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_usuarios);

        db = FirebaseFirestore.getInstance();
        rvUsers = findViewById(R.id.rvUsers);
        progressBar = findViewById(R.id.progressBar);
        fabAddUser = findViewById(R.id.fabAddUser);

        userList = new ArrayList<>();
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new UsuariosAdapter(userList, user -> {
            Intent intent = new Intent(this, DetallesUsuarioActivity.class);
            intent.putExtra("userId", user.getId());
            startActivity(intent);
        });
        rvUsers.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        fabAddUser.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearUsuarioActivity.class));
        });

        loadUsers();
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("users").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        user.setId(document.getId());
                        userList.add(user);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error al cargar usuarios: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }
}