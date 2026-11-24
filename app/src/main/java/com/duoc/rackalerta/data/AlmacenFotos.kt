package com.duoc.rackalerta.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.core.content.FileProvider
import java.io.File
import java.net.URL


object AlmacenFotos {

    private const val LADO_MAXIMO = 1280
    private val fotosDescargadas = LruCache<String, Bitmap>(30)

    fun crearArchivoFoto(context: Context, idRack: Long?): File {
        val etiquetaRack = idRack?.toString() ?: "nuevo"
        return File.createTempFile("rack_${etiquetaRack}_", ".jpg", context.cacheDir)
    }

    fun uriPara(context: Context, archivo: File) =
        FileProvider.getUriForFile(context, context.packageName + ".provider", archivo)

    fun esFotoLocal(rutaFoto: String?): Boolean = rutaFoto?.startsWith("/") == true

    fun comprimir(archivo: File): File {
        val opciones = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(archivo.absolutePath, opciones)
        var reduccion = 1
        while (opciones.outWidth / reduccion > LADO_MAXIMO || opciones.outHeight / reduccion > LADO_MAXIMO) {
            reduccion *= 2
        }
        val foto = BitmapFactory.decodeFile(
            archivo.absolutePath,
            BitmapFactory.Options().apply { inSampleSize = reduccion }
        ) ?: return archivo
        val archivoComprimido = File(archivo.parentFile, "subir_${archivo.name}")
        archivoComprimido.outputStream().use { salida ->
            foto.compress(Bitmap.CompressFormat.JPEG, 80, salida)
        }
        return archivoComprimido
    }

    fun cargarFoto(rutaFoto: String): Bitmap? {
        fotosDescargadas.get(rutaFoto)?.let { return it }
        val foto = try {
            if (esFotoLocal(rutaFoto)) {
                val opciones = BitmapFactory.Options().apply { inSampleSize = 2 }
                BitmapFactory.decodeFile(rutaFoto, opciones)
            } else {
                URL(rutaFoto).openStream().use { entrada -> BitmapFactory.decodeStream(entrada) }
            }
        } catch (error: Exception) {
            null
        }
        foto?.let { fotosDescargadas.put(rutaFoto, it) }
        return foto
    }
}
