package com.duoc.rackalerta.ui.racks
import com.duoc.rackalerta.viewmodel.DetalleRackViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.duoc.rackalerta.ui.components.FotoRack
import com.duoc.rackalerta.repository.RepositorioUsuario
import com.duoc.rackalerta.ui.components.InsigniaEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRackVista(
    idRack: Long,
    repositorioUsuario: RepositorioUsuario,
    alEditarRack: (Long) -> Unit,
    alVolver: () -> Unit
) {
    val modeloVista: DetalleRackViewModel = viewModel(
        factory = viewModelFactory { initializer { DetalleRackViewModel(idRack, repositorioUsuario) } }
    )
    val rack by modeloVista.rack.collectAsState()
    val estadoUi by modeloVista.estadoUi.collectAsState()
    val esEncargado by modeloVista.esEncargado.collectAsState()

    LaunchedEffect(estadoUi.eliminado) {
        if (estadoUi.eliminado) alVolver()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del rack") },
                navigationIcon = { TextButton(onClick = alVolver) { Text("Volver") } }
            )
        }
    ) { innerPadding ->
        val rackActual = rack
        if (rackActual == null) {
            Text(
                "El rack no existe",
                modifier = Modifier.padding(innerPadding).padding(24.dp)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FotoRack(
                rutaFoto = rackActual.rutaFoto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Text(rackActual.nombre, style = MaterialTheme.typography.headlineSmall)
            Text(
                "Número ${rackActual.numero}",
                style = MaterialTheme.typography.bodyLarge
            )
            InsigniaEstado(estado = rackActual.estado)
            Text(
                "Encargado: ${rackActual.encargadoNombre.ifBlank { "Sin encargado" }}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (esEncargado) {
                Button(
                    onClick = { alEditarRack(rackActual.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Actualizar Rack")
                }

                OutlinedButton(
                    onClick = modeloVista::pedirEliminacion,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Eliminar Rack")
                }
            } else {
                Text(
                    "Solo el encargado puede actualizar o eliminar este rack.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (estadoUi.mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = modeloVista::cancelarEliminacion,
            title = { Text("Eliminar rack") },
            text = {
                Column {
                    Text("¿Seguro que quieres eliminar \"${rack?.nombre ?: ""}\" de Xano?")
                    estadoUi.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = modeloVista::confirmarEliminacion,
                    enabled = !estadoUi.eliminando
                ) { Text(if (estadoUi.eliminando) "Eliminando..." else "Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = modeloVista::cancelarEliminacion) { Text("Cancelar") }
            }
        )
    }
}
