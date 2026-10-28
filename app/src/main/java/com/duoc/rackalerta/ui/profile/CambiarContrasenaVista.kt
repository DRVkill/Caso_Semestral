package com.duoc.rackalerta.ui.profile
import com.duoc.rackalerta.ui.viewmodel.CambiarContrasenaViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.duoc.rackalerta.data.RepositorioUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CambiarContrasenaVista(
    repositorioUsuario: RepositorioUsuario,
    alTerminar: () -> Unit,
    alVolver: () -> Unit
) {
    val modeloVista: CambiarContrasenaViewModel = viewModel(
        factory = viewModelFactory { initializer { CambiarContrasenaViewModel(repositorioUsuario) } }
    )
    val estadoUi by modeloVista.estadoUi.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cambiar contraseña") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoContrasena(
                etiqueta = "Contraseña actual",
                valor = estadoUi.contrasenaActual,
                onValueChange = modeloVista::alEscribirActual,
                mensajeError = estadoUi.errorActual,
                imeAction = ImeAction.Next
            )
            CampoContrasena(
                etiqueta = "Nueva contraseña",
                valor = estadoUi.nuevaContrasena,
                onValueChange = modeloVista::alEscribirNueva,
                mensajeError = estadoUi.errorNueva,
                imeAction = ImeAction.Next
            )
            CampoContrasena(
                etiqueta = "Repetir contraseña nueva",
                valor = estadoUi.repetirContrasena,
                onValueChange = modeloVista::alEscribirRepetir,
                mensajeError = estadoUi.errorRepetir,
                imeAction = ImeAction.Done
            )

            Button(
                onClick = modeloVista::enviar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Cambiar contraseña")
            }
        }
    }

    if (estadoUi.mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Contraseña actualizada") },
            text = { Text("Tu contraseña se cambió correctamente.") },
            confirmButton = {
                Button(onClick = {
                    modeloVista.cerrarDialogoExito()
                    alTerminar()
                }) { Text("LISTO") }
            }
        )
    }
}

@Composable
private fun CampoContrasena(
    etiqueta: String,
    valor: String,
    onValueChange: (String) -> Unit,
    mensajeError: String?,
    imeAction: ImeAction
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        singleLine = true,
        isError = mensajeError != null,
        supportingText = { mensajeError?.let { Text(it) } },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
