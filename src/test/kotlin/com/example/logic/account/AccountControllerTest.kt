package com.example.logic.account

import com.example.logic.authorization.logIn.LogInController
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AccountControllerTest {
    private val accountController = AccountController()

    @Test
    fun loadAccount() {
        LogInController().signIn("1", "1111")

        val expected = true
        val actual = accountController.loadAccount()
        assertEquals(expected, actual)
    }
}