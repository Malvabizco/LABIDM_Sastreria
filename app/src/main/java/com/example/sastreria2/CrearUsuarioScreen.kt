package com.example.sastreria2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearUsuarioScreen(
    usuarioDao: UsuarioDao,
    onNavigateBack: (String) -> Unit
) {

    val scope = rememberCoroutineScope()

    var error by remember { mutableStateOf("") }

    // Campos
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Rol
    var expandedRol by remember { mutableStateOf(false) }
    val roles = listOf("Administrador", "Sastre")
    var rolSeleccionado by remember { mutableStateOf("") }

    // Estado
    var expandedEstado by remember { mutableStateOf(false) }
    val estados = listOf("Activo", "Inactivo")
    var estadoSeleccionado by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6A1B9A),
                        Color(0xFF8E24AA)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Taller de costura",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(modifier = Modifier.padding(24.dp)) {

                // 🔙 Encabezado
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onNavigateBack("") },
                        modifier = Modifier.background(Color(0xFFD1E3F3), RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF4A148C))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        "Crear Usuario",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 👤 Nombre
                Text("Nombre completo", color = Color.Gray)
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 📧 Correo
                Text("Correo o usuario", color = Color.Gray)
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Ver contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Ver contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Rol", color = Color.Gray)
                ExposedDropdownMenuBox(
                    expanded = expandedRol,
                    onExpandedChange = { expandedRol = !expandedRol }
                ) {
                    OutlinedTextField(
                        value = rolSeleccionado,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona rol") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRol)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedRol,
                        onDismissRequest = { expandedRol = false }
                    ) {
                        roles.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    rolSeleccionado = it
                                    expandedRol = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Estado", color = Color.Gray)
                ExposedDropdownMenuBox(
                    expanded = expandedEstado,
                    onExpandedChange = { expandedEstado = !expandedEstado }
                ) {
                    OutlinedTextField(
                        value = estadoSeleccionado,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona estado") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedEstado,
                        onDismissRequest = { expandedEstado = false }
                    ) {
                        estados.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    estadoSeleccionado = it
                                    expandedEstado = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {

                        error = ""

                        when {
                            nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                                error = "Completa todos los campos"
                            }

                            rolSeleccionado.isEmpty() -> {
                                error = "Selecciona un rol"
                            }

                            estadoSeleccionado.isEmpty() -> {
                                error = "Selecciona un estado"
                            }

                            password != confirmPassword -> {
                                error = "Las contraseñas no coinciden"
                            }

                            else -> {
                                scope.launch {
                                    usuarioDao.insertarUsuario(
                                        Usuario(
                                            nombreCompleto = nombre,
                                            correo = correo,
                                            contrasena = password,
                                            rol = rolSeleccionado,
                                            estado = estadoSeleccionado
                                        )
                                    )

                                    onNavigateBack("Usuario creado correctamente")
                                }
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Guardar")
                }

                if (error.isNotEmpty()) {
                    Text(error, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { onNavigateBack("") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}