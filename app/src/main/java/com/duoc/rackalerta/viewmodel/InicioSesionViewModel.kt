package com.duoc.rackalerta.viewmodel
import com.duoc.rackalerta.ui.login.ValidadorLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.data.remote.ErroresApi
import com.duoc.rackalerta.repository.RepositorioUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoInicioSesion(
    val correo: String = "",
    val contrasena: String = "",
    val mostrarContrasena: Boolean = false,
    val enviado: Boolean = false,
    val cargando: Boolean = false,
    val errorLogin: String? = null
) {
    val errorCorreo: String?
        get() = if (enviado) ValidadorLogin.errorCorreo(correo) else null

    val errorContrasena: String?
        get() = if (enviado) ValidadorLogin.errorContrasena(contrasena) else null
}

class InicioSesionViewModel(private val repositorioUsuario: RepositorioUsuario) : ViewModel() {

    private val _estadoUi = MutableStateFlow(EstadoInicioSesion())
    val estadoUi: StateFlow<EstadoInicioSesion> = _estadoUi.asStateFlow()

    fun alEscribirCorreo(valor: String) =
        _estadoUi.update { it.copy(correo = valor, errorLogin = null) }

    fun alEscribirContrasena(valor: String) =
        _estadoUi.update { it.copy(contrasena = valor, errorLogin = null) }

    fun alternarMostrarContrasena() =
        _estadoUi.update { it.copy(mostrarContrasena = !it.mostrarContrasena) }

    /** Valida el formulario, inicia sesión en Xano y, si funciona, avisa con alExito. */
    fun enviar(alExito: () -> Unit) {
        val actual = _estadoUi.value
        _estadoUi.update { it.copy(enviado = true) }
        if (actual.cargando ||
            ValidadorLogin.errorCorreo(actual.correo) != null ||
            ValidadorLogin.errorContrasena(actual.contrasena) != null
        ) return

        viewModelScope.launch {
            _estadoUi.update { it.copy(cargando = true, errorLogin = null) }
            try {
                repositorioUsuario.iniciarSesion(actual.correo, actual.contrasena)
                _estadoUi.update { it.copy(cargando = false) }
                alExito()
            } catch (error: Exception) {
                _estadoUi.update {
                    it.copy(
                        cargando = false,
                        errorLogin = ErroresApi.mensaje(
                            error,
                            mensaje401 = "Correo o contraseña incorrectos",
                            mensaje403 = "Correo o contraseña incorrectos"
                        )
                    )
                }
            }
        }
    }
}
