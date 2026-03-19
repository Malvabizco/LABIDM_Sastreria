package com.example.sastreria2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreCompleto: String,
    val correo: String,
    val contrasena: String,
    val rol: String,
    val estado: String
)