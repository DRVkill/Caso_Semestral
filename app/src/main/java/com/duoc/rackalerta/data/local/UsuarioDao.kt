package com.duoc.rackalerta.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(usuario: UsuarioEntidad)

    @Query("SELECT * FROM usuarios LIMIT 1")
    suspend fun obtenerSesion(): UsuarioEntidad?

    @Query("SELECT * FROM usuarios LIMIT 1")
    fun observarSesion(): Flow<UsuarioEntidad?>

    @Query("DELETE FROM usuarios")
    suspend fun borrarSesiones()
}
