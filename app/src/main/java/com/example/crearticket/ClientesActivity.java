package com.example.crearticket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class ClientesActivity extends AppCompatActivity {

    RecyclerView recyclerClientes;
    MaterialButton btnAgregarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        recyclerClientes = findViewById(R.id.recyclerClientes);
        btnAgregarCliente = findViewById(R.id.btnAgregarCliente);

        btnAgregarCliente.setOnClickListener(v -> {
            Toast.makeText(this, "Ir a crear cliente", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CrearClienteActivity.class));
        });
    }
}