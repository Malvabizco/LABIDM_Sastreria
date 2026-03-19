package com.example.sastreria2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConsultarUsuariosScreen(
    usuarioDao: UsuarioDao,
    onAddClick: () -> Unit,
    mensaje: String
) {

    val usuarios by usuarioDao.obtenerTodosLosUsuarios()
        .collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF7B1FA2),
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(70.dp)
            ) {
                Text("+", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6A1B9A),
                            Color(0xFF8E24AA)
                        )
                    )
                )
                .padding(padding)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                Text(
                    text = "Consultar Usuarios",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {

                    Column {

                        if (mensaje.isNotEmpty()) {
                            Text(
                                text = mensaje,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        LazyColumn(modifier = Modifier.padding(12.dp)) {

                            items(usuarios) { user ->

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {

                                    Column(modifier = Modifier.padding(12.dp)) {

                                        Text(
                                            text = user.nombreCompleto,
                                            style = MaterialTheme.typography.bodyLarge
                                        )

                                        Text(
                                            text = user.correo,
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Text(
                                            text = "Rol: ${user.rol}",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Text(
                                            text = "Estado: ${user.estado}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}