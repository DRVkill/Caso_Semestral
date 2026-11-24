package com.duoc.rackalerta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.repository.RepositorioUsuario
import com.duoc.rackalerta.data.local.UsuarioEntidad
import com.duoc.rackalerta.repository.RepositorioRacks
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilViewModel(private val repositorioUsuario: RepositorioUsuario) : ViewModel() {

    val datosUsuario: StateFlow<UsuarioEntidad?> = repositorioUsuario
        .observarSesion()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun cerrarSesionUsuario(alCerrarSesion: () -> Unit) {
        viewModelScope.launch {
            repositorioUsuario.cerrarSesion()
            RepositorioRacks.limpiar()
            alCerrarSesion()
        }
    }
}
