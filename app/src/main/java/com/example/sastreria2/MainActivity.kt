package com.example.sastreria2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.sastreria2.ui.theme.Sastreria2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val dao = db.usuarioDao()

        setContent {

            Sastreria2Theme {

                var pantalla by remember { mutableStateOf("consultar") }
                var mensajeGlobal by remember { mutableStateOf("") }

                if (pantalla == "consultar") {

                    ConsultarUsuariosScreen(
                        usuarioDao = dao,
                        onAddClick = {
                            pantalla = "crear"
                            mensajeGlobal = ""
                        },
                        mensaje = mensajeGlobal
                    )

                } else {

                    CrearUsuarioScreen(
                        usuarioDao = dao, 
                        onNavigateBack = { mensaje ->
                            mensajeGlobal = mensaje
                            pantalla = "consultar"
                        }
                    )
                }
            }
        }
    }
}