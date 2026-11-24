package com.duoc.rackalerta.data.remote

import com.duoc.rackalerta.model.EstadoRack
import com.google.gson.annotations.SerializedName
import com.duoc.rackalerta.model.Rack

data class SolicitudRegistro(
    val name: String,
    val email: String,
    val password: String,
    val cargo: String
)

data class SolicitudLogin(
    val email: String,
    val password: String
)

data class RespuestaToken(
    val authToken: String?
)

data class ClienteRemoto(
    val id: Long?,
    val name: String?,
    val email: String?,
    val cargo: String?
)

data class ImagenXano(
    val url: String?,
    val path: String?
)

data class RackRemoto(
    val id: Long?,
    val nombre: String?,
    val numero: Int?,
    val estado: String?,
    val foto: ImagenXano?,
    @SerializedName("encargado_id") val encargadoId: Long?,
    @SerializedName("encargado_nombre") val encargadoNombre: String?
) {
    fun aRack(): Rack = Rack(
        id = id ?: 0L,
        nombre = nombre ?: "Sin nombre",
        numero = numero ?: 0,
        estado = EstadoRack.desdeTexto(estado),
        rutaFoto = foto?.url ?: foto?.path?.let { ruta -> urlDesdeRuta(ruta) },
        encargadoId = encargadoId ?: 0L,
        encargadoNombre = encargadoNombre ?: ""
    )

    private fun urlDesdeRuta(ruta: String): String {
        val dominio = ConfiguracionXano.URL_RACKS.substringBefore("/api:")
        return dominio + ruta
    }
}
