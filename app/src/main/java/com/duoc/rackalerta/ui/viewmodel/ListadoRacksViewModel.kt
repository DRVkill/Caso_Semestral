package com.duoc.rackalerta.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.rackalerta.data.RepositorioRacks
import com.duoc.rackalerta.model.Rack
import kotlinx.coroutines.flow.StateFlow

class ListadoRacksViewModel : ViewModel() {
    val racks: StateFlow<List<Rack>> = RepositorioRacks.racks
}
