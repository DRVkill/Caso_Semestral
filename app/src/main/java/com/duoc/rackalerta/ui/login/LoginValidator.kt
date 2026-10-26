package com.duoc.rackalerta.ui.login

/** Reglas de validación del formulario de login (lógica pura, fácil de testear). */
object LoginValidator {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    const val MIN_PASSWORD_LENGTH = 6

    // Credenciales de demostración (no hay backend en este caso).
    const val DEMO_EMAIL = "admin@rackalerta.cl"
    const val DEMO_PASSWORD = "123456"

    fun emailError(email: String): String? = when {
        email.isBlank() -> "El correo es obligatorio"
        !EMAIL_REGEX.matches(email.trim()) -> "Ingresa un correo válido"
        else -> null
    }

    fun passwordError(password: String): String? = when {
        password.isBlank() -> "La contraseña es obligatoria"
        password.length < MIN_PASSWORD_LENGTH ->
            "Debe tener al menos $MIN_PASSWORD_LENGTH caracteres"
        else -> null
    }

    fun credentialsMatch(email: String, password: String): Boolean =
        email.trim().equals(DEMO_EMAIL, ignoreCase = true) && password == DEMO_PASSWORD
}
