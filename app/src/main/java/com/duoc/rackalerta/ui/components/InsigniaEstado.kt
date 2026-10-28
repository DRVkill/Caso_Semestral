package com.duoc.rackalerta.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.duoc.rackalerta.model.EstadoRack
import com.duoc.rackalerta.ui.theme.ColorDisponible
import com.duoc.rackalerta.ui.theme.ColorCritico

/** Etiqueta de estado. */
@Composable
fun InsigniaEstado(estado: EstadoRack, modifier: Modifier = Modifier) {
    val colorInsignia by animateColorAsState(
        targetValue = if (estado == EstadoRack.DISPONIBLE) ColorDisponible else ColorCritico,
        label = "colorInsignia"
    )
    Surface(
        color = colorInsignia,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = estado.etiqueta,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
