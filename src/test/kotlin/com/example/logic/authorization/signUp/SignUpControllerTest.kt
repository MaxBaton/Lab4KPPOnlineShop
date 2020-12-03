package com.example.logic.authorization.signUp

import com.example.logic.authorization.tables.Client
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.math.log

internal class SignUpControllerTest {
    private val signUpController = SignUpController()

    @Test
    fun signUp() {
        val name = "maxim"
        val address = "Brest"
        val login = "1"
        val password = "1111"
        val client = Client(name = name, address = address, login = login, password = password)

        val expected = true
        val actual = signUpController.signUp(client)
        assertEquals(expected, actual)
    }
}