package com.duoc.rackalerta

import com.duoc.rackalerta.ui.login.LoginValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginValidatorTest {
    @Test fun emailVacio_da_error() = assertNotNull(LoginValidator.emailError(""))
    @Test fun emailInvalido_da_error() = assertNotNull(LoginValidator.emailError("abc@"))
    @Test fun emailValido_ok() = assertNull(LoginValidator.emailError("user@duoc.cl"))
    @Test fun passwordVacia_da_error() = assertNotNull(LoginValidator.passwordError(""))
    @Test fun passwordCorta_da_error() = assertNotNull(LoginValidator.passwordError("123"))
    @Test fun passwordValida_ok() = assertNull(LoginValidator.passwordError("123456"))
    @Test fun credencialesCorrectas() = assertTrue(
        LoginValidator.credentialsMatch(LoginValidator.DEMO_EMAIL, LoginValidator.DEMO_PASSWORD)
    )
    @Test fun credencialesIncorrectas() =
        assertFalse(LoginValidator.credentialsMatch(LoginValidator.DEMO_EMAIL, "otra"))
}
