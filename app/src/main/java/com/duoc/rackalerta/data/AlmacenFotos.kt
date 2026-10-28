package com.duoc.rackalerta.data

import android.content.Context
import androidx.core.content.FileProvider
import java.io.File

/**
 * Guarda las fotos de los racks en la caché de la app.
 * Cada archivo lleva el id del rack en su nombre para saber a qué rack pertenece.
 */
object AlmacenFotos {

    /** Crea el archivo donde la cámara guardará la foto del rack [idRack] (null si es nuevo). */
    fun crearArchivoFoto(context: Context, idRack: Long?): File {
        val etiquetaRack = idRack?.toString() ?: "nuevo"
        return File.createTempFile("rack_${etiquetaRack}_", ".jpg", context.cacheDir)
    }

    /** Uri que la cámara del sistema puede escribir, usando el FileProvider del manifiesto. */
    fun uriPara(context: Context, archivo: File) =
        FileProvider.getUriForFile(context, context.packageName + ".provider", archivo)
}
