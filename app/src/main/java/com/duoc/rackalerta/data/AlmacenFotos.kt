package com.duoc.rackalerta.data

import android.content.Context
import androidx.core.content.FileProvider
import java.io.File


object AlmacenFotos {

    fun crearArchivoFoto(context: Context, idRack: Long?): File {
        val etiquetaRack = idRack?.toString() ?: "nuevo"
        return File.createTempFile("rack_${etiquetaRack}_", ".jpg", context.cacheDir)
    }

    fun uriPara(context: Context, archivo: File) =
        FileProvider.getUriForFile(context, context.packageName + ".provider", archivo)
}
