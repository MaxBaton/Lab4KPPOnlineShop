package com.example.logic.clientAccount

import com.example.logic.authorization.logIn.LogInController
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ClientAccountControllerTest {
    private val clientAccountController = ClientAccountController()

    @Test
    fun setClientId() {
        LogInController().signIn("1", "1111")

        val expected = true
        val actual = clientAccountController.setClientId()
        assertEquals(expected, actual)
    }
}