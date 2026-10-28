package com.duoc.rackalerta.ui.login

/** Reglas de validación del formulario de login. Lógica pura, fácil de testear. */
object ValidadorLogin {
    const val MIN_LARGO_USUARIO = 3
    const val MIN_LARGO_CONTRASENA = 6

    fun errorUsuario(usuario: String): String? = when {
        usuario.isBlank() -> "El usuario es obligatorio"
        usuario.trim().length < MIN_LARGO_USUARIO ->
            "Debe tener al menos $MIN_LARGO_USUARIO caracteres"
        else -> null
    }

    fun errorContrasena(contrasena: String): String? = when {
        contrasena.isBlank() -> "La contraseña es obligatoria"
        contrasena.length < MIN_LARGO_CONTRASENA ->
            "Debe tener al menos $MIN_LARGO_CONTRASENA caracteres"
        else -> null
    }
}
