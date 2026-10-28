package com.duoc.rackalerta.ui.racks

/** Reglas del formulario de rack. Lógica pura, fácil de testear. */
object ValidadorFormularioRack {
    const val MIN_LARGO_NOMBRE = 3
    const val MIN_NUMERO_RACK = 1
    const val MAX_NUMERO_RACK = 999

    fun errorNombre(nombre: String): String? = when {
        nombre.isBlank() -> "El nombre es obligatorio"
        nombre.trim().length < MIN_LARGO_NOMBRE ->
            "Debe tener al menos $MIN_LARGO_NOMBRE caracteres"
        else -> null
    }

    fun errorNumero(numero: String, numerosTomados: Set<Int>): String? {
        val valor = numero.toIntOrNull()
        return when {
            numero.isBlank() -> "El número es obligatorio"
            valor == null -> "Ingresa solo números"
            valor !in MIN_NUMERO_RACK..MAX_NUMERO_RACK ->
                "Debe estar entre $MIN_NUMERO_RACK y $MAX_NUMERO_RACK"
            valor in numerosTomados -> "Ese número de rack ya existe"
            else -> null
        }
    }
}
