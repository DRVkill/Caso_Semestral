package com.duoc.rackalerta.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.data.RepositorioUsuario
import com.duoc.rackalerta.ui.login.ValidadorLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoCambiarContrasena(
    val contrasenaActual: String = "",
    val nuevaContrasena: String = "",
    val repetirContrasena: String = "",
    val enviado: Boolean = false,
    val contrasenaActualIncorrecta: Boolean = false,
    val mostrarDialogoExito: Boolean = false
) {
    val errorActual: String?
        get() = when {
            !enviado -> null
            contrasenaActual.isBlank() -> "Ingresa tu contraseña actual"
            contrasenaActualIncorrecta -> "La contraseña actual no es correcta"
            else -> null
        }

    val errorNueva: String?
        get() = when {
            !enviado -> null
            ValidadorLogin.errorContrasena(nuevaContrasena) != null -> ValidadorLogin.errorContrasena(nuevaContrasena)
            nuevaContrasena == contrasenaActual -> "Debe ser distinta a la actual"
            else -> null
        }

    val errorRepetir: String?
        get() = if (enviado && repetirContrasena != nuevaContrasena) "Las contraseñas no coinciden" else null
}

class CambiarContrasenaViewModel(private val repositorioUsuario: RepositorioUsuario) : ViewModel() {

    private val _estadoUi = MutableStateFlow(EstadoCambiarContrasena())
    val estadoUi: StateFlow<EstadoCambiarContrasena> = _estadoUi.asStateFlow()

    fun alEscribirActual(valor: String) =
        _estadoUi.update { it.copy(contrasenaActual = valor, contrasenaActualIncorrecta = false) }

    fun alEscribirNueva(valor: String) = _estadoUi.update { it.copy(nuevaContrasena = valor) }

    fun alEscribirRepetir(valor: String) = _estadoUi.update { it.copy(repetirContrasena = valor) }

    /** Valida los campos y compara la contraseña actual contra Room. */
    fun enviar() {
        _estadoUi.update { it.copy(enviado = true) }
        val actual = _estadoUi.value
        if (actual.errorActual != null || actual.errorNueva != null || actual.errorRepetir != null) return

        viewModelScope.launch {
            val usuario = repositorioUsuario.usuarioActual ?: RepositorioUsuario.USUARIO_DEMO
            val usuarioEncontrado = repositorioUsuario.buscarUsuario(usuario)
            if (usuarioEncontrado == null || usuarioEncontrado.contrasena != actual.contrasenaActual) {
                _estadoUi.update { it.copy(contrasenaActualIncorrecta = true) }
                return@launch
            }
            repositorioUsuario.actualizarContrasena(usuario, actual.nuevaContrasena)
            _estadoUi.update { it.copy(mostrarDialogoExito = true) }
        }
    }

    /** Cierra el modal y deja el formulario limpio. */
    fun cerrarDialogoExito() = _estadoUi.update { EstadoCambiarContrasena() }
}
