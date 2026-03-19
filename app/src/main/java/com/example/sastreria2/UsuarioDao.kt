package com.example.sastreria2

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios")
    fun obtenerTodosLosUsuarios(): Flow<List<Usuario>>
}