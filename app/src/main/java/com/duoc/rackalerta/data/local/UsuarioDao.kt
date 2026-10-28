package com.duoc.rackalerta.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarSiNoExiste(usuario: UsuarioEntidad): Long

    @Update
    suspend fun actualizar(usuario: UsuarioEntidad)

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario LIMIT 1")
    suspend fun buscarPorUsuario(usuario: String): UsuarioEntidad?

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario LIMIT 1")
    fun observarPorUsuario(usuario: String): Flow<UsuarioEntidad?>
}
