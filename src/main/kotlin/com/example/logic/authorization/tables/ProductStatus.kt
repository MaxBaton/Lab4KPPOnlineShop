package com.example.logic.authorization.tables

enum class ProductStatus(val statusName: String) {
    PAID("Оплачен"),
    IN_TRANSIT("В пути"),
    ARRIVED("Прибыл"),
    RECEIVED("Получен")
}