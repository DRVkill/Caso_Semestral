package com.duoc.rackalerta.repository

import com.duoc.rackalerta.data.local.UsuarioDao
import com.duoc.rackalerta.data.local.UsuarioEntidad
import com.duoc.rackalerta.data.remote.ClienteApi
import com.duoc.rackalerta.data.remote.SolicitudLogin
import com.duoc.rackalerta.data.remote.SolicitudRegistro
import kotlinx.coroutines.flow.Flow

/** Los clientes se registran e inician sesión en Xano. La sesión (token) se guarda en Room. */
class RepositorioUsuario(private val usuarioDao: UsuarioDao) {

    suspend fun cargarSesionGuardada(): UsuarioEntidad? {
        val sesion = usuarioDao.obtenerSesion()
        ClienteApi.token = sesion?.token
        return sesion
    }

    fun observarSesion(): Flow<UsuarioEntidad?> = usuarioDao.observarSesion()

    suspend fun iniciarSesion(correo: String, contrasena: String) {
        val respuesta = ClienteApi.clientes.iniciarSesion(
            SolicitudLogin(email = correo.trim().lowercase(), password = contrasena)
        )
        guardarSesion(respuesta.authToken)
    }

    suspend fun registrar(nombreCompleto: String, correo: String, contrasena: String, cargo: String) {
        val respuesta = ClienteApi.clientes.registrar(
            SolicitudRegistro(
                name = nombreCompleto.trim(),
                email = correo.trim().lowercase(),
                password = contrasena,
                cargo = cargo.trim()
            )
        )
        guardarSesion(respuesta.authToken)
    }

    suspend fun cerrarSesion() {
        usuarioDao.borrarSesiones()
        ClienteApi.token = null
    }

    private suspend fun guardarSesion(token: String?) {
        if (token.isNullOrBlank()) throw IllegalStateException("Xano no entregó un token")
        ClienteApi.token = token
        try {
            val cliente = ClienteApi.clientes.obtenerClienteActual()
            usuarioDao.borrarSesiones()
            usuarioDao.guardar(
                UsuarioEntidad(
                    correo = cliente.email ?: "",
                    idCliente = cliente.id ?: 0L,
                    nombreCompleto = cliente.name ?: "",
                    token = token,
                    cargo = cliente.cargo?.takeIf { it.isNotBlank() } ?: "Cliente Rackalerta"
                )
            )
        } catch (error: Exception) {
            ClienteApi.token = null
            throw error
        }
    }
}
