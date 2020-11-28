package com.example.logic.authorization.tables

data class Client(var id: Int = 0, val name: String, val address: String,
                  val login: String, val password: String, var status: String = ClientStatus.USUAL.satusName)