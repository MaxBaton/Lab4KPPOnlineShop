package com.example.logic.clientAccount

import com.example.logic.account.AccountController
import com.example.logic.authorization.database.ConstClient
import com.example.logic.authorization.database.DatabaseHandler
import com.example.logic.authorization.tables.Account
import com.example.logic.authorization.tables.Client
import com.example.logic.logs.LogApp
import com.example.view.client.ClientAccountView
import com.example.view.client.LogInView
import javafx.scene.text.Text
import sun.rmi.runtime.Log
import tornadofx.Controller
import java.lang.Exception
import java.sql.ResultSet
import java.sql.SQLException
import java.util.logging.Level

class ClientAccountController : Controller() {
    private val myLog = LogApp.getLogger()

    companion object {
        var client: Client? = null
    }

    fun setStatus(txtClientStatus: Text): Boolean {
        val name: String = client?.name ?: "EMPTY"
        val status: String = client?.status ?: "EMPTY"
        txtClientStatus.text = "$name - $status"
        return (name.isNotEmpty() && status.isNotEmpty())
   }

    fun setClientId(): Boolean {
        try {
            val _client = client
            val select = "SELECT * FROM ${ConstClient.CLIENT_TABLE} WHERE ${ConstClient.CLIENT_LOGIN} = ? " +
                    "AND ${ConstClient.CLIENT_PASSWORD} = ?"

            val preparedStatement2 = DatabaseHandler().getDbConnection().prepareStatement(select)
            preparedStatement2.setString(1, _client!!.login)
            preparedStatement2.setString(2, _client.password)
            val resultSet = preparedStatement2.executeQuery()
            val data = mutableListOf<String>()

            while (resultSet.next()) {
                resultSet.metaData.columnCount
                for (i in 1..1) {
                    val row = resultSet.getString(i)
                    data.add(row)
                }
            }

            val id = data[0].toInt()
            client!!.id = id

            AccountController.account = Account(client = _client!!)

            myLog.log(Level.INFO, "ClientAccountController: Установка id=$id клиента прошла успешно")
            if (resultSet != null) return true
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "ClientAccountController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "ClientAccountController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "ClientAccountController: Exception", e)
        }

        return false
    }
}