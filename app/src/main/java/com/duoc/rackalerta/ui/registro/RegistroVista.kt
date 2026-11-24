package com.duoc.rackalerta.ui.registro
import com.duoc.rackalerta.viewmodel.RegistroViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.duoc.rackalerta.repository.RepositorioUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroVista(
    repositorioUsuario: RepositorioUsuario,
    alRegistrarse: () -> Unit,
    alVolver: () -> Unit
) {
    val modeloVista: RegistroViewModel = viewModel(
        factory = viewModelFactory { initializer { RegistroViewModel(repositorioUsuario) } }
    )
    val estadoUi by modeloVista.estadoUi.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta") },
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
            CampoRegistro(
                etiqueta = "Nombre completo",
                valor = estadoUi.nombre,
                onValueChange = modeloVista::alEscribirNombre,
                mensajeError = estadoUi.errorNombre,
                tipoTeclado = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
            CampoRegistro(
                etiqueta = "Cargo",
                valor = estadoUi.cargo,
                onValueChange = modeloVista::alEscribirCargo,
                mensajeError = estadoUi.errorCargo,
                tipoTeclado = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
            CampoRegistro(
                etiqueta = "Correo",
                valor = estadoUi.correo,
                onValueChange = modeloVista::alEscribirCorreo,
                mensajeError = estadoUi.errorCorreo,
                tipoTeclado = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            CampoRegistro(
                etiqueta = "Contraseña (mínimo 8, letras y números)",
                valor = estadoUi.contrasena,
                onValueChange = modeloVista::alEscribirContrasena,
                mensajeError = estadoUi.errorContrasena,
                tipoTeclado = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
            CampoRegistro(
                etiqueta = "Repetir contraseña",
                valor = estadoUi.repetirContrasena,
                onValueChange = modeloVista::alEscribirRepetir,
                mensajeError = estadoUi.errorRepetir,
                tipoTeclado = KeyboardType.Password,
                imeAction = ImeAction.Done
            )

            estadoUi.errorRegistro?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = modeloVista::enviar,
                enabled = !estadoUi.cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (estadoUi.cargando) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                } else {
                    Text("Crear cuenta")
                }
            }
        }
    }

    if (estadoUi.mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Cuenta creada") },
            text = { Text("Bienvenido, ${estadoUi.nombre.trim()}. Tu cuenta quedó registrada.") },
            confirmButton = {
                Button(onClick = alRegistrarse) { Text("COMENZAR") }
            }
        )
    }
}

@Composable
private fun CampoRegistro(
    etiqueta: String,
    valor: String,
    onValueChange: (String) -> Unit,
    mensajeError: String?,
    tipoTeclado: KeyboardType,
    imeAction: ImeAction
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        singleLine = true,
        isError = mensajeError != null,
        supportingText = { mensajeError?.let { Text(it) } },
        visualTransformation =
            if (tipoTeclado == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            imeAction = imeAction
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
