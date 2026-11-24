package com.duoc.rackalerta.data.remote

import retrofit2.HttpException
import java.io.IOException

object ErroresApi {

    fun esSesionExpirada(error: Throwable): Boolean =
        error is HttpException && error.code() == 401

    fun mensaje(
        error: Throwable,
        mensaje401: String = "Tu sesión expiró, vuelve a iniciar sesión",
        mensaje403: String = "Solo el encargado de este rack puede modificarlo"
    ): String = when {
        error is HttpException && error.code() == 401 -> mensaje401
        error is HttpException && error.code() == 403 -> mensaje403
        error is HttpException && error.code() == 404 -> "No se encontró el rack o el endpoint en Xano"
        error is HttpException -> "Error del servidor (${error.code()})"
        error is IOException -> "No hay conexión con el servidor"
        else -> "Ocurrió un error inesperado"
    }
}
