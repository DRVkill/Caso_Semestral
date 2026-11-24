package com.duoc.rackalerta.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiClientes {

    @POST("auth/signup")
    suspend fun registrar(@Body solicitud: SolicitudRegistro): RespuestaToken

    @POST("auth/login")
    suspend fun iniciarSesion(@Body solicitud: SolicitudLogin): RespuestaToken

    @GET("auth/me")
    suspend fun obtenerClienteActual(): ClienteRemoto
}
