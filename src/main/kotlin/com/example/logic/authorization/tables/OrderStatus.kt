package com.example.logic.authorization.tables

enum class OrderStatus(val statusName: String) {
    PAID("Оплачен"),
    ORDERED("Заказан")
}