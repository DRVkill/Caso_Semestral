package com.duoc.rackalerta.ui.racks
import com.duoc.rackalerta.viewmodel.ListadoRacksViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.duoc.rackalerta.model.Rack
import com.duoc.rackalerta.ui.components.FotoRack
import com.duoc.rackalerta.ui.components.InsigniaEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoRacksVista(
    alClicRack: (Long) -> Unit,
    alNuevoRack: () -> Unit,
    alClicPerfil: () -> Unit,
    alSesionExpirada: () -> Unit,
    modeloVista: ListadoRacksViewModel = viewModel()
) {
    val racks by modeloVista.racks.collectAsState()
    val estadoUi by modeloVista.estadoUi.collectAsState()

    LaunchedEffect(estadoUi.sesionExpirada) {
        if (estadoUi.sesionExpirada) alSesionExpirada()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Racks") },
                actions = {
                    TextButton(onClick = modeloVista::recargar) { Text("Recargar") }
                    TextButton(onClick = alClicPerfil) { Text("Perfil") }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = alNuevoRack,
                icon = { Text("+") },
                text = { Text("Nuevo rack") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (estadoUi.cargando) {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            }
            estadoUi.error?.let { mensaje ->
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(mensaje, color = MaterialTheme.colorScheme.error)
                        TextButton(onClick = modeloVista::recargar) { Text("Reintentar") }
                    }
                }
            }
            if (racks.isEmpty() && !estadoUi.cargando && estadoUi.error == null) {
                item { Text("Aún no hay racks registrados en Xano") }
            }
            items(items = racks, key = { it.id }) { rack ->
                TarjetaRack(
                    rack = rack,
                    onClick = { alClicRack(rack.id) },
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
private fun TarjetaRack(rack: Rack, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            FotoRack(rutaFoto = rack.rutaFoto, modifier = Modifier.size(72.dp))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(rack.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Número ${rack.numero}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (rack.encargadoNombre.isNotBlank()) {
                    Text(
                        "Encargado: ${rack.encargadoNombre}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            InsigniaEstado(estado = rack.estado)
        }
    }
}
