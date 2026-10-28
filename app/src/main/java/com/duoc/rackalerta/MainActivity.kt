package com.duoc.rackalerta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.duoc.rackalerta.data.RepositorioUsuario
import com.duoc.rackalerta.data.local.BaseDatosApp
import com.duoc.rackalerta.ui.navigation.NavegacionRackalerta
import com.duoc.rackalerta.ui.theme.TemaRackalerta

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val baseDatos = BaseDatosApp.crear(this)
        val repositorioUsuario = RepositorioUsuario(baseDatos.usuarioDao())

        setContent {
            TemaRackalerta {
                NavegacionRackalerta(repositorioUsuario = repositorioUsuario)
            }
        }
    }
}
