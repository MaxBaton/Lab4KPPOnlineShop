package com.example.logic.admin.adminProducts

import com.example.logic.authorization.tables.Product
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AdminProductsControllerTest {
    private val adminProductsController = AdminProductsController()

    @Test
    fun changeNumOfProduct() {
        val productId = 1
        val numOfProduct = 230

        val expected = true
        val actual = adminProductsController.changeNumOfProduct(productId = productId, newNumOfProduct = numOfProduct)
        assertEquals(expected, actual)
    }

    @Test
    fun changeCostOfProduct() {
        val productId = 1
        val costOfProduct = 30

        val expected = true
        val actual = adminProductsController.changeCostOfProduct(productId = productId, newCostOfProduct = costOfProduct)
        assertEquals(expected, actual)
    }

    @Test
    fun createNewProduct() {
        val product = Product(name = "s", country = "s", cost = 23, numberInStock = 33)

        val expected = true
        val actual = adminProductsController.createNewProduct(product)
        assertEquals(expected, actual)
    }

    @Test
    fun deleteProduct() {
        val productId = 124

        val expected = true
        val actual = adminProductsController.deleteProduct(productId = productId)
        assertEquals(expected, actual)
    }
}