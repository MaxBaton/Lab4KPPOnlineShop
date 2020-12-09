package com.example.logic.myOrders

import com.example.logic.authorization.logIn.LogInController
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MyOrdersControllerTest {
    private val myOrdersController = MyOrdersController()

    @Test
    fun getListOfOrders() {
        LogInController().signIn("1", "1111")

        val actual = myOrdersController.getListOfOrders()
        assertNotNull(actual)
    }
}