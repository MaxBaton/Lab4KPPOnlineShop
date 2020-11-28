package com.example.logic.authorization.tables

data class Order(val number: Int = 0, var id: Int = 0, val date: String, val totalCost: Int, val allProducts: String,
                 val client: Client? = null, var status: String)