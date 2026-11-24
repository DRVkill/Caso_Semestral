package com.duoc.rackalerta

import com.duoc.rackalerta.ui.login.ValidadorLogin
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorLoginTest {
    @Test fun correoVacio_da_error() = assertNotNull(ValidadorLogin.errorCorreo(""))
    @Test fun correoSinArroba_da_error() = assertNotNull(ValidadorLogin.errorCorreo("admin.rackalerta.cl"))
    @Test fun correoValido_ok() = assertNull(ValidadorLogin.errorCorreo("admin@rackalerta.cl"))
    @Test fun contrasenaVacia_da_error() = assertNotNull(ValidadorLogin.errorContrasena(""))
    @Test fun contrasenaCorta_da_error() = assertNotNull(ValidadorLogin.errorContrasena("123"))
    @Test fun contrasenaValida_ok() = assertNull(ValidadorLogin.errorContrasena("clave1234"))
    @Test fun contrasenaNuevaSinNumeros_da_error() = assertNotNull(ValidadorLogin.errorContrasenaNueva("soloLetras"))
    @Test fun contrasenaNuevaValida_ok() = assertNull(ValidadorLogin.errorContrasenaNueva("rack2026ok"))
    @Test fun cargoVacio_da_error() = assertNotNull(ValidadorLogin.errorCargo("  "))
    @Test fun nombreCorto_da_error() = assertNotNull(ValidadorLogin.errorNombre("Al"))
    @Test fun contrasenasDistintas_da_error() = assertNotNull(ValidadorLogin.errorRepetir("clave1234", "clave9999"))
}
