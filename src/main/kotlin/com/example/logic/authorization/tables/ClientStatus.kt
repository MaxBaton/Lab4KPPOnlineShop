package com.example.logic.authorization.tables

enum class ClientStatus(val satusName: String) {
    USUAL("Обычный пользователь"),
    IN_BLACK_LIST("В черном списке")
}