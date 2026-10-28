package com.duoc.rackalerta.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Usuario guardado en Room. Se persiste entre reinicios de la app. */
@Entity(tableName = "usuarios")
data class UsuarioEntidad(
    @PrimaryKey val usuario: String,
    val contrasena: String,
    val nombreCompleto: String,
    val cargo: String,
    val correo: String
)
