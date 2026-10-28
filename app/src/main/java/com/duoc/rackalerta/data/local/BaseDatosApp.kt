package com.duoc.rackalerta.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UsuarioEntidad::class], version = 1, exportSchema = false)
abstract class BaseDatosApp : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        fun crear(context: Context): BaseDatosApp =
            Room.databaseBuilder(context, BaseDatosApp::class.java, "rackalerta.db")
                .build()
    }
}
