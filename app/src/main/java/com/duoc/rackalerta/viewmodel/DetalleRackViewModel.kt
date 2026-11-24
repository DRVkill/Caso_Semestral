package com.duoc.rackalerta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.data.remote.ErroresApi
import com.duoc.rackalerta.repository.RepositorioRacks
import com.duoc.rackalerta.repository.RepositorioUsuario
import com.duoc.rackalerta.model.Rack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoDetalleRack(
    val mostrarDialogoEliminar: Boolean = false,
    val eliminando: Boolean = false,
    val eliminado: Boolean = false,
    val error: String? = null
)

class DetalleRackViewModel(
    private val idRack: Long,
    repositorioUsuario: RepositorioUsuario
) : ViewModel() {
    val rack: StateFlow<Rack?> = RepositorioRacks.observarRack(idRack).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RepositorioRacks.buscarRack(idRack)
    )

    /** Es true solo si el cliente con sesión iniciada es el encargado del rack. */
    val esEncargado: StateFlow<Boolean> = combine(
        RepositorioRacks.observarRack(idRack),
        repositorioUsuario.observarSesion()
    ) { rackActual, sesion ->
        rackActual != null && sesion != null && rackActual.encargadoId == sesion.idCliente
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private val _estadoUi = MutableStateFlow(EstadoDetalleRack())
    val estadoUi: StateFlow<EstadoDetalleRack> = _estadoUi.asStateFlow()

    fun pedirEliminacion() = _estadoUi.update { it.copy(mostrarDialogoEliminar = true, error = null) }

    fun cancelarEliminacion() = _estadoUi.update { it.copy(mostrarDialogoEliminar = false) }

    fun confirmarEliminacion() {
        if (_estadoUi.value.eliminando) return
        viewModelScope.launch {
            _estadoUi.update { it.copy(eliminando = true, error = null) }
            try {
                RepositorioRacks.eliminarRack(idRack)
                _estadoUi.update { it.copy(eliminando = false, mostrarDialogoEliminar = false, eliminado = true) }
            } catch (error: Exception) {
                _estadoUi.update {
                    it.copy(eliminando = false, error = ErroresApi.mensaje(error, mensaje403 = "Solo el encargado de este rack puede eliminarlo"))
                }
            }
        }
    }
}
