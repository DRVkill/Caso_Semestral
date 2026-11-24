package com.duoc.rackalerta.repository

import com.duoc.rackalerta.data.remote.ClienteApi
import com.duoc.rackalerta.model.EstadoRack
import com.duoc.rackalerta.model.Rack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

/** Inventario de racks guardado en Xano. Aquí se mantiene la última lista descargada. */
object RepositorioRacks {

    private val estadoRacks = MutableStateFlow<List<Rack>>(emptyList())

    val racks: StateFlow<List<Rack>> = estadoRacks

    fun observarRack(idRack: Long): Flow<Rack?> =
        estadoRacks.map { list -> list.firstOrNull { it.id == idRack } }

    fun buscarRack(idRack: Long): Rack? = estadoRacks.value.firstOrNull { it.id == idRack }

    suspend fun recargarRacks() {
        estadoRacks.value = ClienteApi.racks.listarRacks()
            .map { rackRemoto -> rackRemoto.aRack() }
            .sortedBy { rack -> rack.numero }
    }

    suspend fun agregarRack(nombre: String, numero: Int, estado: EstadoRack, archivoFoto: File?) {
        ClienteApi.racks.crearRack(
            nombre = texto(nombre.trim()),
            numero = texto(numero.toString()),
            estado = texto(estado.name),
            foto = parteFoto(archivoFoto)
        )
        recargarRacks()
    }

    suspend fun actualizarRack(
        idRack: Long,
        nombre: String,
        numero: Int,
        estado: EstadoRack,
        archivoFoto: File?
    ) {
        ClienteApi.racks.actualizarRack(
            idRack = idRack,
            nombre = texto(nombre.trim()),
            numero = texto(numero.toString()),
            estado = texto(estado.name),
            foto = parteFoto(archivoFoto)
        )
        recargarRacks()
    }

    suspend fun eliminarRack(idRack: Long) {
        val respuesta = ClienteApi.racks.eliminarRack(idRack)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
        estadoRacks.update { list -> list.filterNot { it.id == idRack } }
    }

    fun limpiar() {
        estadoRacks.value = emptyList()
    }

    private fun texto(valor: String): RequestBody = valor.toRequestBody("text/plain".toMediaType())

    private fun parteFoto(archivo: File?): MultipartBody.Part? = archivo?.let {
        MultipartBody.Part.createFormData("foto", it.name, it.asRequestBody("image/jpeg".toMediaType()))
    }
}
