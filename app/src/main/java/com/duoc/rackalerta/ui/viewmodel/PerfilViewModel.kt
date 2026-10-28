package com.duoc.rackalerta.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.rackalerta.data.RepositorioUsuario
import com.duoc.rackalerta.data.local.UsuarioEntidad
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PerfilViewModel(private val repositorioUsuario: RepositorioUsuario) : ViewModel() {

    val datosUsuario: StateFlow<UsuarioEntidad?> = repositorioUsuario
        .observarUsuario(repositorioUsuario.usuarioActual ?: RepositorioUsuario.USUARIO_DEMO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun cerrarSesionUsuario(alCerrarSesion: () -> Unit) {
        repositorioUsuario.terminarSesion()
        alCerrarSesion()
    }
}
