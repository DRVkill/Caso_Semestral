package com.duoc.rackalerta.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteApi {

    @Volatile
    var token: String? = null

    private val clienteHttp: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { cadena ->
            val solicitud = cadena.request().newBuilder()
            token?.let { tokenGuardado -> solicitud.addHeader("Authorization", "Bearer $tokenGuardado") }
            cadena.proceed(solicitud.build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    val clientes: ApiClientes by lazy {
        crearRetrofit(ConfiguracionXano.URL_CLIENTES).create(ApiClientes::class.java)
    }

    val racks: ApiRacks by lazy {
        crearRetrofit(ConfiguracionXano.URL_RACKS).create(ApiRacks::class.java)
    }

    private fun crearRetrofit(urlBase: String): Retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .client(clienteHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
