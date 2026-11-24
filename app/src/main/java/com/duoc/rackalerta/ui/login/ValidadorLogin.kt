package com.duoc.rackalerta.ui.login


/** Requisitos para validación del login y del registro de clientes. */
object ValidadorLogin {
    const val MIN_LARGO_NOMBRE = 3
    const val MIN_LARGO_CONTRASENA = 8

    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun errorNombre(nombre: String): String? = when {
        nombre.isBlank() -> "El nombre es obligatorio"
        nombre.trim().length < MIN_LARGO_NOMBRE ->
            "Debe tener al menos $MIN_LARGO_NOMBRE caracteres"
        else -> null
    }

    fun errorCargo(cargo: String): String? =
        if (cargo.isBlank()) "El cargo es obligatorio" else null

    fun errorCorreo(correo: String): String? = when {
        correo.isBlank() -> "El correo es obligatorio"
        !formatoCorreo.matches(correo.trim()) -> "El correo no es válido"
        else -> null
    }

    fun errorContrasena(contrasena: String): String? = when {
        contrasena.isBlank() -> "La contraseña es obligatoria"
        contrasena.length < MIN_LARGO_CONTRASENA ->
            "Debe tener al menos $MIN_LARGO_CONTRASENA caracteres"
        else -> null
    }

    fun errorContrasenaNueva(contrasena: String): String? = when {
        errorContrasena(contrasena) != null -> errorContrasena(contrasena)
        contrasena.none { it.isLetter() } || contrasena.none { it.isDigit() } ->
            "Debe tener letras y números"
        else -> null
    }

    fun errorRepetir(contrasena: String, repetir: String): String? =
        if (contrasena != repetir) "Las contraseñas no coinciden" else null
}
