package com.duoc.rackalerta.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Sesión del cliente guardada en Room. Se mantiene aunque la app se cierre o se elimine de segundo plano. */
@Entity(tableName = "usuarios")
data class UsuarioEntidad(
    @PrimaryKey val correo: String,
    val idCliente: Long,
    val nombreCompleto: String,
    val token: String,
    val cargo: String = "Cliente Rackalerta"
)
