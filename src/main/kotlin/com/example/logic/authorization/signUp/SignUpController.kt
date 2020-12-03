package com.example.logic.authorization.signUp

import com.example.logic.authorization.database.ConstClient
import com.example.logic.authorization.database.DatabaseHandler
import com.example.logic.authorization.tables.Client
import com.example.logic.clientAccount.ClientAccountController
import com.example.logic.logs.LogApp
import tornadofx.Controller
import java.lang.Exception
import java.sql.SQLException
import java.util.logging.Level

class SignUpController: Controller() {
    private val myLog = LogApp.getLogger()

    fun signUp(client: Client): Boolean {
        val insertClient = "INSERT INTO ${ConstClient.CLIENT_TABLE}(${ConstClient.CLIENT_NAME}," +
                "${ConstClient.CLIENT_ADDRESS},${ConstClient.CLIENT_LOGIN},${ConstClient.CLIENT_PASSWORD}," +
                "${ConstClient.CLIENT_STATUS})VALUES(?,?,?,?,?)"

        val listOfLogins = getLoginsFromClients()
        val listOfPasswords = getPasswordsFromClients()

        val mapOfLoginPassword = mutableMapOf<String, String>()
        listOfLogins.forEachIndexed { index, s ->
            mapOfLoginPassword[s] = listOfPasswords[index]
        }

        mapOfLoginPassword.forEach {
            if (client.login == it.key && client.password == it.value) return false
        }

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(insertClient)
            preparedStatement.setString(1, client.name)
            preparedStatement.setString(2, client.address)
            preparedStatement.setString(3, client.login)
            preparedStatement.setString(4, client.password)
            preparedStatement.setString(5, client.status)

            preparedStatement.executeUpdate()

            ClientAccountController.client = client

            myLog.log(Level.INFO,"SignUpController: Пользователь $client успешно зарешичтрирован")
            if (preparedStatement != null) return true
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "SignUpController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "SignUpController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "SignUpController: Exception", e)
        }

        return false
    }

    private fun getLoginsFromClients(): List<String> {
        var listOfLogins = listOf<String>()

        val selectLoginPassword = "SELECT ${ConstClient.CLIENT_LOGIN} FROM ${ConstClient.CLIENT_TABLE}"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().
                                                                    prepareStatement(selectLoginPassword)
            val resultSet = preparedStatement.executeQuery()
            val data = mutableListOf<String>()

            while (resultSet.next()) {
                val columnCount = resultSet.metaData.columnCount
                for (i in 1..columnCount) {
                    val row = resultSet.getString(i)
                    data.add(row)
                }
            }

            listOfLogins = data.toList()

            myLog.log(Level.INFO, "SignUpController: Логины $listOfLogins получены")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "SignUpController: SQ:Exception", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "SignUpController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "SignUpController: Exception", e)
        }

        return listOfLogins
    }

    private fun getPasswordsFromClients(): List<String> {
        var listOfPasswords = listOf<String>()

        val selectLoginPassword = "SELECT ${ConstClient.CLIENT_PASSWORD} FROM ${ConstClient.CLIENT_TABLE}"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().
            prepareStatement(selectLoginPassword)
            val resultSet = preparedStatement.executeQuery()
            val data = mutableListOf<String>()

            while (resultSet.next()) {
                val columnCount = resultSet.metaData.columnCount
                for (i in 1..columnCount) {
                    val row = resultSet.getString(i)
                    data.add(row)
                }
            }

            listOfPasswords = data.toList()

            myLog.log(Level.INFO, "SignUpController: Пароли $listOfPasswords получены")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "SignUpController: SQ:Exception", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "SignUpController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "SignUpController: Exception", e)
        }

        return listOfPasswords
    }
}