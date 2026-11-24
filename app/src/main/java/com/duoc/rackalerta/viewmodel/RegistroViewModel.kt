package com.duoc.rackalerta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.data.remote.ErroresApi
import com.duoc.rackalerta.repository.RepositorioUsuario
import com.duoc.rackalerta.ui.login.ValidadorLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoRegistro(
    val nombre: String = "",
    val cargo: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val repetirContrasena: String = "",
    val enviado: Boolean = false,
    val cargando: Boolean = false,
    val errorRegistro: String? = null,
    val mostrarDialogoExito: Boolean = false
) {
    val errorNombre: String?
        get() = if (enviado) ValidadorLogin.errorNombre(nombre) else null

    val errorCargo: String?
        get() = if (enviado) ValidadorLogin.errorCargo(cargo) else null

    val errorCorreo: String?
        get() = if (enviado) ValidadorLogin.errorCorreo(correo) else null

    val errorContrasena: String?
        get() = if (enviado) ValidadorLogin.errorContrasenaNueva(contrasena) else null

    val errorRepetir: String?
        get() = if (enviado) ValidadorLogin.errorRepetir(contrasena, repetirContrasena) else null

    val tieneErrores: Boolean
        get() = listOf(errorNombre, errorCargo, errorCorreo, errorContrasena, errorRepetir).any { it != null }
}

class RegistroViewModel(private val repositorioUsuario: RepositorioUsuario) : ViewModel() {

    private val _estadoUi = MutableStateFlow(EstadoRegistro())
    val estadoUi: StateFlow<EstadoRegistro> = _estadoUi.asStateFlow()

    fun alEscribirNombre(valor: String) = _estadoUi.update { it.copy(nombre = valor, errorRegistro = null) }

    fun alEscribirCargo(valor: String) = _estadoUi.update { it.copy(cargo = valor) }

    fun alEscribirCorreo(valor: String) = _estadoUi.update { it.copy(correo = valor, errorRegistro = null) }

    fun alEscribirContrasena(valor: String) = _estadoUi.update { it.copy(contrasena = valor) }

    fun alEscribirRepetir(valor: String) = _estadoUi.update { it.copy(repetirContrasena = valor) }

    /** Valida los campos y registra al cliente en Xano. */
    fun enviar() {
        _estadoUi.update { it.copy(enviado = true) }
        val actual = _estadoUi.value
        if (actual.tieneErrores || actual.cargando) return

        viewModelScope.launch {
            _estadoUi.update { it.copy(cargando = true, errorRegistro = null) }
            try {
                repositorioUsuario.registrar(actual.nombre, actual.correo, actual.contrasena, actual.cargo)
                _estadoUi.update { it.copy(cargando = false, mostrarDialogoExito = true) }
            } catch (error: Exception) {
                _estadoUi.update {
                    it.copy(
                        cargando = false,
                        errorRegistro = ErroresApi.mensaje(error, mensaje403 = "Ese correo ya está registrado")
                    )
                }
            }
        }
    }
}
