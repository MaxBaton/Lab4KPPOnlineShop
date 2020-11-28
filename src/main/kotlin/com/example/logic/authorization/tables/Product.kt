package com.example.logic.authorization.tables

data class Product(val number: Int = 0,var id: Int = 0, val name: String, var cost: Int, val country: String, var numberInStock: Int,
                   var numOfOrder: Int = 0, val order: Order? = null)