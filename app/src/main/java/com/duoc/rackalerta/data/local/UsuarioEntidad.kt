package com.duoc.rackalerta.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** El usuario se guarda en room y se mantiene entre los reinicios de app.*/
@Entity(tableName = "usuarios")
data class UsuarioEntidad(
    @PrimaryKey val usuario: String,
    val contrasena: String,
    val nombreCompleto: String,
    val cargo: String,
    val correo: String
)
