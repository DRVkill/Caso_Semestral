package com.duoc.rackalerta.ui.racks
import com.duoc.rackalerta.ui.viewmodel.FormularioRackViewModel

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.duoc.rackalerta.data.AlmacenFotos
import com.duoc.rackalerta.model.EstadoRack
import com.duoc.rackalerta.ui.components.FotoRack
import com.duoc.rackalerta.ui.components.InsigniaEstado
import com.duoc.rackalerta.ui.theme.ColorDisponible
import com.duoc.rackalerta.ui.theme.ColorCritico
import java.io.File


/** Formulario para creación o edición de rack, idRack es null si se crea uno nuevo.*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioRackVista(
    idRack: Long?,
    alGuardar: () -> Unit,
    alVolver: () -> Unit
) {
    val modeloVista: FormularioRackViewModel = viewModel(
        key = "rack_form_${idRack ?: "new"}",
        factory = viewModelFactory { initializer { FormularioRackViewModel(idRack) } }
    )
    val estadoUi by modeloVista.estadoUi.collectAsState()
    val errores = modeloVista.validar(estadoUi)
    val context = LocalContext.current


    /** Aquí la cámara escribe la foto que se está tomando.*/
    var fotoPendiente by remember { mutableStateOf<File?>(null) }

    val lanzadorCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { fotoTomada ->
        val archivo = fotoPendiente
        if (fotoTomada && archivo != null) modeloVista.alCapturarFoto(archivo.absolutePath)
    }

    /** Se pide permiso primero, al conceder se abre la cámara.*/
    val lanzadorPermisoCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val archivo = AlmacenFotos.crearArchivoFoto(context, idRack)
            fotoPendiente = archivo
            lanzadorCamara.launch(AlmacenFotos.uriPara(context, archivo))
        }
    }

    LaunchedEffect(estadoUi.guardado) {
        if (estadoUi.guardado) alGuardar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (estadoUi.esRackNuevo) "Nuevo rack" else "Actualizar rack")
                },
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
            FotoRack(
                rutaFoto = estadoUi.rutaFoto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Button(
                onClick = { lanzadorPermisoCamara.launch(Manifest.permission.CAMERA) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(if (estadoUi.rutaFoto == null) "Tomar foto del rack" else "Volver a tomar foto")
            }

            errores.foto?.takeIf { estadoUi.enviado }?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            /** Aparece una alerta animada cuando la foto asigna un estado..*/

            AnimatedVisibility(
                visible = estadoUi.estado != null,
                enter = fadeIn(tween(400)) + expandVertically(),
                exit = fadeOut(tween(200)) + shrinkVertically()
            ) {
                estadoUi.estado?.let { TarjetaAlertaEstado(it) }
            }

            /** Si es un rack nuevo, los campos solo aparecen al tomar la foto..*/

            AnimatedVisibility(
                visible = !estadoUi.esRackNuevo || estadoUi.rutaFoto != null,
                enter = fadeIn(tween(400)) + expandVertically(),
                exit = fadeOut(tween(200)) + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = estadoUi.nombre,
                        onValueChange = modeloVista::alEscribirNombre,
                        label = { Text("Nombre del rack") },
                        singleLine = true,
                        isError = estadoUi.enviado && errores.nombre != null,
                        supportingText = {
                            if (estadoUi.enviado) errores.nombre?.let { Text(it) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = estadoUi.numero,
                        onValueChange = modeloVista::alEscribirNumero,
                        label = { Text("Número del rack") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        isError = estadoUi.enviado && errores.numero != null,
                        supportingText = {
                            if (estadoUi.enviado) errores.numero?.let { Text(it) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            OutlinedButton(
                onClick = modeloVista::solicitarGuardado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Guardar rack")
            }
        }
    }

    if (estadoUi.mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = modeloVista::cerrarDialogoConfirmacion,
            title = { Text("Confirmar rack") },
            text = {
                Text(
                    "¿Guardar \"${estadoUi.nombre.trim()}\" (Número ${estadoUi.numero}) " +
                        "con estado ${estadoUi.estado?.etiqueta ?: "pendiente"}?"
                )
            },
            confirmButton = {
                Button(onClick = modeloVista::confirmarGuardado) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = modeloVista::cerrarDialogoConfirmacion) { Text("Cancelar") }
            }
        )
    }
}

/** Tarjeta de alerta que cambia de color según el estado asignado. */

@Composable
private fun TarjetaAlertaEstado(estado: EstadoRack) {
    val colorTarjeta by animateColorAsState(
        targetValue = if (estado == EstadoRack.DISPONIBLE) ColorDisponible else ColorCritico,
        animationSpec = tween(400),
        label = "alertCardColor"
    )
    Card(
        colors = CardDefaults.cardColors(containerColor = colorTarjeta),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (estado == EstadoRack.DISPONIBLE)
                    "Rack en buen estado" else "Alerta: rack en estado crítico",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            InsigniaEstado(estado = estado, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
