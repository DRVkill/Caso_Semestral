package com.duoc.rackalerta.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.duoc.rackalerta.R
import com.duoc.rackalerta.ui.theme.AzulSuave


/** Muestra la foto del rack que se guardó local. Si no tiene, se muestra el logo de este como placeholder. */

@Composable
fun FotoRack(rutaFoto: String?, modifier: Modifier = Modifier) {
    val foto = remember(rutaFoto) {
        rutaFoto?.let { BitmapFactory.decodeFile(it)?.asImageBitmap() }
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AzulSuave),
        contentAlignment = Alignment.Center
    ) {
        if (foto != null) {
            Image(
                bitmap = foto,
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
