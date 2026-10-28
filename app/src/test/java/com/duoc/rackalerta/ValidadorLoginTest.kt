package com.duoc.rackalerta

import com.duoc.rackalerta.ui.login.ValidadorLogin
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorLoginTest {
    @Test fun usuarioVacio_da_error() = assertNotNull(ValidadorLogin.errorUsuario(""))
    @Test fun usuarioMuyCorto_da_error() = assertNotNull(ValidadorLogin.errorUsuario("ab"))
    @Test fun usuarioValido_ok() = assertNull(ValidadorLogin.errorUsuario("admin"))
    @Test fun contrasenaVacia_da_error() = assertNotNull(ValidadorLogin.errorContrasena(""))
    @Test fun contrasenaCorta_da_error() = assertNotNull(ValidadorLogin.errorContrasena("123"))
    @Test fun contrasenaValida_ok() = assertNull(ValidadorLogin.errorContrasena("123456"))
}
