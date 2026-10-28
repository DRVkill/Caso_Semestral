package com.duoc.rackalerta.ui.profile
import com.duoc.rackalerta.ui.viewmodel.PerfilViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.duoc.rackalerta.data.RepositorioUsuario
import com.duoc.rackalerta.ui.theme.AzulMarino
import com.duoc.rackalerta.ui.theme.AzulCielo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilVista(
    repositorioUsuario: RepositorioUsuario,
    alIrCambiarContrasena: () -> Unit,
    alCerrarSesion: () -> Unit,
    alVolver: () -> Unit
) {
    val modeloVista: PerfilViewModel = viewModel(
        factory = viewModelFactory { initializer { PerfilViewModel(repositorioUsuario) } }
    )
    val datosUsuario by modeloVista.datosUsuario.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = { TextButton(onClick = alVolver) { Text("Volver") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            datosUsuario?.let { perfil ->
                FilaDatoPerfil(etiqueta = "Nombre", valor = perfil.nombreCompleto)
                FilaDatoPerfil(etiqueta = "Cargo", valor = perfil.cargo)
                FilaDatoPerfil(etiqueta = "Correo", valor = perfil.correo)

                TarjetaCredencial(
                    nombre = perfil.nombreCompleto,
                    cargo = perfil.cargo,
                    usuario = perfil.usuario
                )
            }

            Button(
                onClick = alIrCambiarContrasena,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Cambiar contraseña")
            }

            OutlinedButton(
                onClick = { modeloVista.cerrarSesionUsuario(alCerrarSesion) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun FilaDatoPerfil(etiqueta: String, valor: String) {
    Column {
        Text(etiqueta, style = MaterialTheme.typography.labelMedium)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}

/** Credencial digital del usuario, con estilo de tarjeta de identificación. */
@Composable
private fun TarjetaCredencial(nombre: String, cargo: String, usuario: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AzulMarino),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Credencial Digital", color = AzulCielo, style = MaterialTheme.typography.labelLarge)
                Text(nombre, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(cargo, color = Color.White, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = usuario.uppercase(),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
