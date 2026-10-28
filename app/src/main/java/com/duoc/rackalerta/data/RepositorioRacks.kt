package com.duoc.rackalerta.data

import com.duoc.rackalerta.model.Rack
import com.duoc.rackalerta.model.EstadoRack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Inventario de racks en memoria. Los 5 racks iniciales están definidos
 * en código; los que se crean después se pierden al reiniciar la app.
 */
object RepositorioRacks {

    private val racksPorDefecto: List<Rack> = listOf(
        Rack(id = 1, nombre = "Rack #1", numero = 1, estado = EstadoRack.CRITICO, rutaFoto = null),
        Rack(id = 2, nombre = "Rack #2", numero = 2, estado = EstadoRack.DISPONIBLE, rutaFoto = null),
        Rack(id = 3, nombre = "Rack #3", numero = 3, estado = EstadoRack.DISPONIBLE, rutaFoto = null),
        Rack(id = 4, nombre = "Rack #4", numero = 4, estado = EstadoRack.DISPONIBLE, rutaFoto = null),
        Rack(id = 5, nombre = "Rack #5", numero = 5, estado = EstadoRack.DISPONIBLE, rutaFoto = null)
    )

    private val estadoRacks = MutableStateFlow(racksPorDefecto)
    private var proximoId: Long = racksPorDefecto.size + 1L

    val racks: StateFlow<List<Rack>> = estadoRacks

    fun observarRack(idRack: Long): Flow<Rack?> =
        estadoRacks.map { list -> list.firstOrNull { it.id == idRack } }

    fun buscarRack(idRack: Long): Rack? = estadoRacks.value.firstOrNull { it.id == idRack }

    fun agregarRack(nombre: String, numero: Int, estado: EstadoRack, rutaFoto: String?): Rack {
        val rack = Rack(
            id = proximoId++,
            nombre = nombre.trim(),
            numero = numero,
            estado = estado,
            rutaFoto = rutaFoto
        )
        estadoRacks.update { it + rack }
        return rack
    }

    fun actualizarRack(idRack: Long, nombre: String, numero: Int, estado: EstadoRack, rutaFoto: String?) {
        estadoRacks.update { list ->
            list.map { rack ->
                if (rack.id == idRack) {
                    rack.copy(nombre = nombre.trim(), numero = numero, estado = estado, rutaFoto = rutaFoto)
                } else {
                    rack
                }
            }
        }
    }
}
