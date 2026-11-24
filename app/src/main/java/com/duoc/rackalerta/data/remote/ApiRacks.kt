package com.duoc.rackalerta.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiRacks {

    @GET("racks")
    suspend fun listarRacks(): List<RackRemoto>

    @Multipart
    @POST("racks")
    suspend fun crearRack(
        @Part("nombre") nombre: RequestBody,
        @Part("numero") numero: RequestBody,
        @Part("estado") estado: RequestBody,
        @Part foto: MultipartBody.Part?
    ): RackRemoto

    @Multipart
    @PATCH("racks/{idRack}")
    suspend fun actualizarRack(
        @Path("idRack") idRack: Long,
        @Part("nombre") nombre: RequestBody,
        @Part("numero") numero: RequestBody,
        @Part("estado") estado: RequestBody,
        @Part foto: MultipartBody.Part?
    ): RackRemoto

    @DELETE("racks/{idRack}")
    suspend fun eliminarRack(@Path("idRack") idRack: Long): Response<Unit>
}
