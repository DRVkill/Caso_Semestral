package com.duoc.rackalerta.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.duoc.rackalerta.R
import com.duoc.rackalerta.data.AlmacenFotos
import com.duoc.rackalerta.ui.theme.AzulSuave
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/** Muestra la foto del rack (desde Xano o recién tomada). Si no tiene, se muestra el logo como placeholder. */

@Composable
fun FotoRack(rutaFoto: String?, modifier: Modifier = Modifier) {
    val foto by produceState<Bitmap?>(initialValue = null, rutaFoto) {
        value = rutaFoto?.let { ruta -> withContext(Dispatchers.IO) { AlmacenFotos.cargarFoto(ruta) } }
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AzulSuave),
        contentAlignment = Alignment.Center
    ) {
        val fotoCargada = foto
        if (fotoCargada != null) {
            Image(
                bitmap = fotoCargada.asImageBitmap(),
                contentDescription = "Foto del rack",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(R.drawable.logo_rack),
                contentDescription = "Sin foto",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
