package com.example.logic.admin.adminClients

data class ClientForAdmin(val id: Int = 0,var number: Int, val name: String, val address: String,
                          var status: String, val accountAmount: Int)