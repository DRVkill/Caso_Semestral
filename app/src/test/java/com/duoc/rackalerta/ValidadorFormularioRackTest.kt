package com.duoc.rackalerta

import com.duoc.rackalerta.data.remote.RackRemoto
import com.duoc.rackalerta.model.EstadoRack
import com.duoc.rackalerta.ui.racks.ValidadorFormularioRack
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidadorFormularioRackTest {
    @Test fun nombreVacio_da_error() = assertNotNull(ValidadorFormularioRack.errorNombre("  "))
    @Test fun nombreCorto_da_error() = assertNotNull(ValidadorFormularioRack.errorNombre("ab"))
    @Test fun nombreValido_ok() = assertNull(ValidadorFormularioRack.errorNombre("Rack Norte"))

    @Test fun numeroVacio_da_error() = assertNotNull(ValidadorFormularioRack.errorNumero("", emptySet()))
    @Test fun numeroNoNumerico_da_error() = assertNotNull(ValidadorFormularioRack.errorNumero("abc", emptySet()))
    @Test fun numeroCero_da_error() = assertNotNull(ValidadorFormularioRack.errorNumero("0", emptySet()))
    @Test fun numeroRepetido_da_error() = assertNotNull(ValidadorFormularioRack.errorNumero("3", setOf(3)))
    @Test fun numeroNuevo_ok() = assertNull(ValidadorFormularioRack.errorNumero("6", setOf(1, 2, 3, 4, 5)))

    @Test fun estadoAleatorio_es_uno_de_los_valores() {
        val estado = EstadoRack.estadoAleatorio()
        assertTrue(estado == EstadoRack.DISPONIBLE || estado == EstadoRack.CRITICO)
    }

    @Test fun etiquetas_en_espanol() {
        assertEquals("Disponible", EstadoRack.DISPONIBLE.etiqueta)
        assertEquals("Crítico", EstadoRack.CRITICO.etiqueta)
    }

    @Test fun estadoDesdeXano_se_convierte() {
        assertEquals(EstadoRack.CRITICO, EstadoRack.desdeTexto("CRITICO"))
        assertEquals(EstadoRack.DISPONIBLE, EstadoRack.desdeTexto("disponible"))
    }

    @Test fun estadoDesconocido_queda_disponible() =
        assertEquals(EstadoRack.DISPONIBLE, EstadoRack.desdeTexto(null))

    @Test fun rackDeXano_conserva_encargado() {
        val rack = RackRemoto(
            id = 3, nombre = "Rack A", numero = 7, estado = "CRITICO", foto = null,
            encargadoId = 2, encargadoNombre = "Ana Pérez"
        ).aRack()
        assertEquals(2L, rack.encargadoId)
        assertEquals("Ana Pérez", rack.encargadoNombre)
        assertEquals(EstadoRack.CRITICO, rack.estado)
    }
}
