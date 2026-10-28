package com.duoc.rackalerta.ui.login
import com.duoc.rackalerta.ui.viewmodel.InicioSesionViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.duoc.rackalerta.R
import com.duoc.rackalerta.data.RepositorioUsuario
import com.duoc.rackalerta.ui.theme.AzulMarino

@Composable
fun InicioSesionVista(
    repositorioUsuario: RepositorioUsuario,
    alIniciarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modeloVista: InicioSesionViewModel = viewModel(
        factory = viewModelFactory { initializer { InicioSesionViewModel(repositorioUsuario) } }
    )
    val estadoUi by modeloVista.estadoUi.collectAsState()
    val focusManager = LocalFocusManager.current

    fun intentarInicioSesion() {
        focusManager.clearFocus()
        modeloVista.enviar(alExito = alIniciarSesion)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AzulMarino)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RACK-ALERTA",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
        Image(
            painter = painterResource(R.drawable.logo_rack),
            contentDescription = "Logo de rack",
            modifier = Modifier
                .padding(vertical = 24.dp)
                .size(140.dp)
        )

        OutlinedTextField(
            value = estadoUi.usuario,
            onValueChange = modeloVista::alEscribirUsuario,
            label = { Text("Usuario") },
            singleLine = true,
            isError = estadoUi.errorUsuario != null,
            supportingText = { estadoUi.errorUsuario?.let { Text(it, color = Color.White) } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            colors = coloresCampoLogin(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = estadoUi.contrasena,
            onValueChange = modeloVista::alEscribirContrasena,
            label = { Text("Contraseña") },
            singleLine = true,
            isError = estadoUi.errorContrasena != null,
            supportingText = { estadoUi.errorContrasena?.let { Text(it, color = Color.White) } },
            visualTransformation =
                if (estadoUi.mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = modeloVista::alternarMostrarContrasena) {
                    Text(if (estadoUi.mostrarContrasena) "Ocultar" else "Mostrar", color = Color.White)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { intentarInicioSesion() }),
            colors = coloresCampoLogin(),
            modifier = Modifier.fillMaxWidth()
        )

        estadoUi.errorLogin?.let {
            Text(
                text = it,
                color = Color(0xFFFFB4AB),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = ::intentarInicioSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Iniciar Sesión", fontSize = 16.sp)
        }
    }
}

@Composable
private fun coloresCampoLogin() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
    cursorColor = Color.White
)
