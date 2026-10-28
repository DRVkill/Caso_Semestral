package com.duoc.rackalerta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.rackalerta.data.RepositorioUsuario
import com.duoc.rackalerta.ui.login.InicioSesionVista
import com.duoc.rackalerta.ui.racks.DetalleRackVista
import com.duoc.rackalerta.ui.racks.FormularioRackVista
import com.duoc.rackalerta.ui.profile.CambiarContrasenaVista
import com.duoc.rackalerta.ui.profile.PerfilVista
import com.duoc.rackalerta.ui.racks.ListadoRacksVista

/** Rutas de la app. */
object Rutas {
    const val INICIO_SESION = "inicio_sesion"
    const val LISTA_RACKS = "lista_racks"
    const val DETALLE_RACK = "detalle_rack/{idRack}"
    const val FORMULARIO_RACK = "formulario_rack/{idRack}"
    const val PERFIL = "perfil"
    const val CAMBIAR_CONTRASENA = "cambiar_contrasena"

    /** Valor de idRack que indica que el formulario crea un rack nuevo. */
    const val ID_RACK_NUEVO = -1L

    fun rutaDetalleRack(idRack: Long) = "detalle_rack/$idRack"
    fun rutaFormularioRack(idRack: Long = ID_RACK_NUEVO) = "formulario_rack/$idRack"
}

@Composable
fun NavegacionRackalerta(repositorioUsuario: RepositorioUsuario) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.INICIO_SESION) {

        composable(Rutas.INICIO_SESION) {
            InicioSesionVista(
                repositorioUsuario = repositorioUsuario,
                alIniciarSesion = {
                    navController.navigate(Rutas.LISTA_RACKS) {
                        popUpTo(Rutas.INICIO_SESION) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.LISTA_RACKS) {
            ListadoRacksVista(
                alClicRack = { idRack -> navController.navigate(Rutas.rutaDetalleRack(idRack)) },
                alNuevoRack = { navController.navigate(Rutas.rutaFormularioRack()) },
                alClicPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }

        composable(Rutas.PERFIL) {
            PerfilVista(
                repositorioUsuario = repositorioUsuario,
                alIrCambiarContrasena = { navController.navigate(Rutas.CAMBIAR_CONTRASENA) },
                alCerrarSesion = {
                    navController.navigate(Rutas.INICIO_SESION) {
                        popUpTo(Rutas.LISTA_RACKS) { inclusive = true }
                    }
                },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.CAMBIAR_CONTRASENA) {
            CambiarContrasenaVista(
                repositorioUsuario = repositorioUsuario,
                alTerminar = { navController.popBackStack() },
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
