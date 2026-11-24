package com.duoc.rackalerta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.data.remote.ErroresApi
import com.duoc.rackalerta.repository.RepositorioRacks
import com.duoc.rackalerta.model.Rack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoListadoRacks(
    val cargando: Boolean = false,
    val error: String? = null,
    val sesionExpirada: Boolean = false
)

class ListadoRacksViewModel : ViewModel() {
    val racks: StateFlow<List<Rack>> = RepositorioRacks.racks

    private val _estadoUi = MutableStateFlow(EstadoListadoRacks())
    val estadoUi: StateFlow<EstadoListadoRacks> = _estadoUi.asStateFlow()

    init {
        recargar()
    }

    fun recargar() {
        viewModelScope.launch {
            _estadoUi.update { it.copy(cargando = true, error = null) }
            try {
                RepositorioRacks.recargarRacks()
                _estadoUi.update { it.copy(cargando = false) }
            } catch (error: Exception) {
                _estadoUi.update {
                    it.copy(
                        cargando = false,
                        error = ErroresApi.mensaje(error),
                        sesionExpirada = ErroresApi.esSesionExpirada(error)
                    )
                }
            }
        }
    }
}
