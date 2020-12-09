package com.example.logic.makeOrder

import com.example.logic.authorization.logIn.LogInController
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MakeOrderControllerTest {
    private val makeOrderController = MakeOrderController()

    @Test
    fun getListOfProducts() {
        val actual = makeOrderController.getListOfProducts()
        assertNotNull(actual)
    }
}