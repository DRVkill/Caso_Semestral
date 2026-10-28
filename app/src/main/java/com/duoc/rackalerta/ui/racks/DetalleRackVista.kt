package com.duoc.rackalerta.ui.racks
import com.duoc.rackalerta.ui.viewmodel.DetalleRackViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.duoc.rackalerta.ui.components.FotoRack
import com.duoc.rackalerta.ui.components.InsigniaEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRackVista(
    idRack: Long,
    alEditarRack: (Long) -> Unit,
    alVolver: () -> Unit
) {
    val modeloVista: DetalleRackViewModel = viewModel(
        factory = viewModelFactory { initializer { DetalleRackViewModel(idRack) } }
    )
    val rack by modeloVista.rack.collectAsState()

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

            Button(
                onClick = { alEditarRack(rackActual.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Actualizar Rack")
            }
        }
    }
}
