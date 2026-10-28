package com.duoc.rackalerta.data

import com.duoc.rackalerta.data.local.UsuarioDao
import com.duoc.rackalerta.data.local.UsuarioEntidad
import kotlinx.coroutines.flow.Flow

/** Acceso a los datos del usuario guardados en Room. */
class RepositorioUsuario(private val usuarioDao: UsuarioDao) {

    /** Usuario que tiene sesión iniciada, pero solo existe mientras la app se encuentre abierta. */
    var usuarioActual: String? = null
        private set

    fun iniciarSesion(usuario: String) {
        usuarioActual = usuario
    }

    fun terminarSesion() {
        usuarioActual = null
    }

    /** Crear usuario demo al abrir la app por primera vez */
    suspend fun crearUsuarioDemoSiFalta() {
        usuarioDao.insertarSiNoExiste(
            UsuarioEntidad(
                usuario = USUARIO_DEMO,
                contrasena = CONTRASENA_DEMO,
                nombreCompleto = "Administrador Rackalerta",
                cargo = "Técnico de infraestructura",
                correo = "admin@rackalerta.cl"
            )
        )
    }

    suspend fun buscarUsuario(usuario: String): UsuarioEntidad? =
        usuarioDao.buscarPorUsuario(usuario.trim())

    fun observarUsuario(usuario: String): Flow<UsuarioEntidad?> =
        usuarioDao.observarPorUsuario(usuario)

    suspend fun actualizarContrasena(usuario: String, nuevaContrasena: String) {
        val usuarioEncontrado = usuarioDao.buscarPorUsuario(usuario) ?: return
        usuarioDao.actualizar(usuarioEncontrado.copy(contrasena = nuevaContrasena))
    }

    companion object {
        const val USUARIO_DEMO = "admin"
        const val CONTRASENA_DEMO = "123456"
    }
}
