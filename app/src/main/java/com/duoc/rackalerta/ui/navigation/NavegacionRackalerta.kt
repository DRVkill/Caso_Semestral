package com.duoc.rackalerta.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.rackalerta.repository.RepositorioRacks
import com.duoc.rackalerta.repository.RepositorioUsuario
import com.duoc.rackalerta.ui.login.InicioSesionVista
import com.duoc.rackalerta.ui.racks.DetalleRackVista
import com.duoc.rackalerta.ui.racks.FormularioRackVista
import com.duoc.rackalerta.ui.profile.PerfilVista
import com.duoc.rackalerta.ui.racks.ListadoRacksVista
import com.duoc.rackalerta.ui.registro.RegistroVista
import kotlinx.coroutines.launch

/** Rutas de la app. */
object Rutas {
    const val INICIO_SESION = "inicio_sesion"
    const val REGISTRO = "registro"
    const val LISTA_RACKS = "lista_racks"
    const val DETALLE_RACK = "detalle_rack/{idRack}"
    const val FORMULARIO_RACK = "formulario_rack/{idRack}"
    const val PERFIL = "perfil"

    /** Valor de idRack que indica que el formulario crea un rack nuevo. */
    const val ID_RACK_NUEVO = -1L

    fun rutaDetalleRack(idRack: Long) = "detalle_rack/$idRack"
    fun rutaFormularioRack(idRack: Long = ID_RACK_NUEVO) = "formulario_rack/$idRack"
}

/** Si hay una sesión guardada en Room, la app parte directo en el listado (como YouTube). */
@Composable
fun NavegacionRackalerta(repositorioUsuario: RepositorioUsuario) {
    var rutaInicial by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val sesion = repositorioUsuario.cargarSesionGuardada()
        rutaInicial = if (sesion != null) Rutas.LISTA_RACKS else Rutas.INICIO_SESION
    }

    val inicio = rutaInicial
    if (inicio == null) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
        return
    }

    val navController = rememberNavController()
    val alcance = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = inicio) {

        composable(Rutas.INICIO_SESION) {
            InicioSesionVista(
                repositorioUsuario = repositorioUsuario,
                alIniciarSesion = { irAlListadoLimpiandoHistorial(navController) },
                alIrRegistro = { navController.navigate(Rutas.REGISTRO) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroVista(
                repositorioUsuario = repositorioUsuario,
                alRegistrarse = { irAlListadoLimpiandoHistorial(navController) },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.LISTA_RACKS) {
            ListadoRacksVista(
                alClicRack = { idRack -> navController.navigate(Rutas.rutaDetalleRack(idRack)) },
                alNuevoRack = { navController.navigate(Rutas.rutaFormularioRack()) },
                alClicPerfil = { navController.navigate(Rutas.PERFIL) },
                alSesionExpirada = {
                    alcance.launch {
                        repositorioUsuario.cerrarSesion()
                        RepositorioRacks.limpiar()
                        irAlLoginLimpiandoHistorial(navController)
                    }
                }
            )
        }

        composable(Rutas.PERFIL) {
            PerfilVista(
                repositorioUsuario = repositorioUsuario,
                alCerrarSesion = { irAlLoginLimpiandoHistorial(navController) },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.DETALLE_RACK,
            arguments = listOf(navArgument("idRack") { type = NavType.LongType })
        ) { backStackEntry ->
            val idRack = backStackEntry.arguments?.getLong("idRack") ?: return@composable
            DetalleRackVista(
                idRack = idRack,
                repositorioUsuario = repositorioUsuario,
                alEditarRack = { id -> navController.navigate(Rutas.rutaFormularioRack(id)) },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.FORMULARIO_RACK,
            arguments = listOf(navArgument("idRack") { type = NavType.LongType })
        ) { backStackEntry ->
            val idRack = backStackEntry.arguments?.getLong("idRack") ?: Rutas.ID_RACK_NUEVO
            FormularioRackVista(
                idRack = idRack.takeIf { it != Rutas.ID_RACK_NUEVO },
                alGuardar = { navController.popBackStack() },
                alVolver = { navController.popBackStack() }
            )
        }
    }
}

private fun irAlListadoLimpiandoHistorial(navController: NavHostController) {
    navController.navigate(Rutas.LISTA_RACKS) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}

private fun irAlLoginLimpiandoHistorial(navController: NavHostController) {
    navController.navigate(Rutas.INICIO_SESION) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}
