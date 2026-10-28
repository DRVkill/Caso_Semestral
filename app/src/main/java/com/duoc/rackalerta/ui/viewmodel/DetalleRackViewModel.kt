package com.duoc.rackalerta.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.data.RepositorioRacks
import com.duoc.rackalerta.model.Rack
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DetalleRackViewModel(idRack: Long) : ViewModel() {
    val rack: StateFlow<Rack?> = RepositorioRacks.observarRack(idRack).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RepositorioRacks.buscarRack(idRack)
    )
}
