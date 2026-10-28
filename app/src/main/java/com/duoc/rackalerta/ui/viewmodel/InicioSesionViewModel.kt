package com.duoc.rackalerta.ui.viewmodel
import com.duoc.rackalerta.ui.login.ValidadorLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.data.RepositorioUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoInicioSesion(
    val usuario: String = "",
    val contrasena: String = "",
    val mostrarContrasena: Boolean = false,
    val enviado: Boolean = false,
    val errorLogin: String? = null
) {
    // Los errores de campo solo aparecen después del primer intento de ingreso.
    val errorUsuario: String?
        get() = if (enviado) ValidadorLogin.errorUsuario(usuario) else null

    val errorContrasena: String?
        get() = if (enviado) ValidadorLogin.errorContrasena(contrasena) else null
}

class InicioSesionViewModel(private val repositorioUsuario: RepositorioUsuario) : ViewModel() {

    private val _estadoUi = MutableStateFlow(EstadoInicioSesion())
    val estadoUi: StateFlow<EstadoInicioSesion> = _estadoUi.asStateFlow()

    fun alEscribirUsuario(valor: String) =
        _estadoUi.update { it.copy(usuario = valor, errorLogin = null) }

    fun alEscribirContrasena(valor: String) =
        _estadoUi.update { it.copy(contrasena = valor, errorLogin = null) }

    fun alternarMostrarContrasena() =
        _estadoUi.update { it.copy(mostrarContrasena = !it.mostrarContrasena) }

    /** Valida el formulario, compara con Room y, si coincide, avisa con [alExito]. */
    fun enviar(alExito: () -> Unit) {
        val actual = _estadoUi.value
        _estadoUi.update { it.copy(enviado = true) }
        if (ValidadorLogin.errorUsuario(actual.usuario) != null ||
            ValidadorLogin.errorContrasena(actual.contrasena) != null
        ) return

        viewModelScope.launch {
            // Garantiza que el usuario de demo exista antes de comparar.
            repositorioUsuario.crearUsuarioDemoSiFalta()
            val usuarioEncontrado = repositorioUsuario.buscarUsuario(actual.usuario)
            if (usuarioEncontrado != null && usuarioEncontrado.contrasena == actual.contrasena) {
                repositorioUsuario.iniciarSesion(usuarioEncontrado.usuario)
                alExito()
            } else {
                _estadoUi.update { it.copy(errorLogin = "Usuario o contraseña incorrectos") }
            }
        }
    }
}
