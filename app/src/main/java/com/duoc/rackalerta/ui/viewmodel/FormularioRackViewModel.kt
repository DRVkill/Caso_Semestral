package com.duoc.rackalerta.ui.viewmodel
import com.duoc.rackalerta.ui.racks.ValidadorFormularioRack

import androidx.lifecycle.ViewModel
import com.duoc.rackalerta.data.RepositorioRacks
import com.duoc.rackalerta.model.EstadoRack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Estado del formulario. idRack es null al crear un rack nuevo. */
data class EstadoFormularioRack(
    val idRack: Long? = null,
    val nombre: String = "",
    val numero: String = "",
    val estado: EstadoRack? = null,
    val rutaFoto: String? = null,
    val enviado: Boolean = false,
    val mostrarDialogoConfirmacion: Boolean = false,
    val guardado: Boolean = false
) {
    val esRackNuevo: Boolean get() = idRack == null
}

/** Errores del formulario. Un valor nulo significa que el campo es válido. */
data class ErroresFormularioRack(
    val nombre: String?,
    val numero: String?,
    val foto: String?
) {
    val tieneErrores: Boolean get() = listOf(nombre, numero, foto).any { it != null }
}

class FormularioRackViewModel(idRack: Long?) : ViewModel() {

    private val _estadoUi = MutableStateFlow(estadoInicial(idRack))
    val estadoUi: StateFlow<EstadoFormularioRack> = _estadoUi.asStateFlow()

    private fun estadoInicial(idRack: Long?): EstadoFormularioRack {
        val existente = idRack?.let { RepositorioRacks.buscarRack(it) }
            ?: return EstadoFormularioRack()
        return EstadoFormularioRack(
            idRack = existente.id,
            nombre = existente.nombre,
            numero = existente.numero.toString(),
            estado = existente.estado,
            rutaFoto = existente.rutaFoto
        )
    }

    /** Calcula los errores según el estado actual. */
    fun validar(actual: EstadoFormularioRack): ErroresFormularioRack {
        // Al editar, el número propio del rack no cuenta como duplicado.
        val numerosTomados = RepositorioRacks.racks.value
            .filter { it.id != actual.idRack }
            .map { it.numero }
            .toSet()
        return ErroresFormularioRack(
            nombre = ValidadorFormularioRack.errorNombre(actual.nombre),
            numero = ValidadorFormularioRack.errorNumero(actual.numero, numerosTomados),
            foto = if (actual.esRackNuevo && actual.rutaFoto == null)
                "Debes tomar una foto del rack" else null
        )
    }

    fun alEscribirNombre(valor: String) = _estadoUi.update { it.copy(nombre = valor) }

    fun alEscribirNumero(valor: String) =
        _estadoUi.update { it.copy(numero = valor.filter(Char::isDigit).take(3)) }

    /** Al tomar una foto, el estado del rack se asigna de forma aleatoria. */
    fun alCapturarFoto(rutaFoto: String) = _estadoUi.update {
        it.copy(rutaFoto = rutaFoto, estado = EstadoRack.estadoAleatorio())
    }


    fun solicitarGuardado() {
        _estadoUi.update { it.copy(enviado = true) }
        if (!validar(_estadoUi.value).tieneErrores) {
            _estadoUi.update { it.copy(mostrarDialogoConfirmacion = true) }
        }
    }

    fun cerrarDialogoConfirmacion() = _estadoUi.update { it.copy(mostrarDialogoConfirmacion = false) }

    fun confirmarGuardado() {
        val actual = _estadoUi.value
        val estado = actual.estado ?: return
        val numero = actual.numero.toInt()
        val nombre = actual.nombre.trim()

        val idRack = actual.idRack
        if (idRack == null) {
            RepositorioRacks.agregarRack(nombre, numero, estado, actual.rutaFoto)
        } else {
            RepositorioRacks.actualizarRack(idRack, nombre, numero, estado, actual.rutaFoto)
        }
        _estadoUi.update { it.copy(mostrarDialogoConfirmacion = false, guardado = true) }
    }
}
