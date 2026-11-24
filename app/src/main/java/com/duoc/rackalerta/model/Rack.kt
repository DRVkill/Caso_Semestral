package com.duoc.rackalerta.model

import kotlin.random.Random


enum class EstadoRack(val etiqueta: String) {
    DISPONIBLE("Disponible"),
    CRITICO("Crítico");

    companion object {
        fun estadoAleatorio(): EstadoRack = if (Random.nextBoolean()) DISPONIBLE else CRITICO

        fun desdeTexto(texto: String?): EstadoRack =
            entries.firstOrNull { it.name.equals(texto, ignoreCase = true) || it.etiqueta.equals(texto, ignoreCase = true) }
                ?: DISPONIBLE
    }
}

/** Rack del inventario. rutaFoto es la URL de la foto en Xano, o la ruta local mientras aún no se sube. Solo el encargado puede modificarlo. */
data class Rack(
    val id: Long,
    val nombre: String,
    val numero: Int,
    val estado: EstadoRack,
    val rutaFoto: String?,
    val encargadoId: Long = 0L,
    val encargadoNombre: String = ""
)
