package com.duoc.rackalerta.model

import kotlin.random.Random

/** Estado de un rack. El valor se asigna de forma aleatoria al tomar la foto. */
enum class EstadoRack(val etiqueta: String) {
    DISPONIBLE("Disponible"),
    CRITICO("Crítico");

    companion object {
        fun estadoAleatorio(): EstadoRack = if (Random.nextBoolean()) DISPONIBLE else CRITICO
    }
}

/**
 * Rack del inventario.
 * [rutaFoto] apunta a la foto guardada en el almacenamiento temporal de la app.
 */
data class Rack(
    val id: Long,
    val nombre: String,
    val numero: Int,
    val estado: EstadoRack,
    val rutaFoto: String?
)
