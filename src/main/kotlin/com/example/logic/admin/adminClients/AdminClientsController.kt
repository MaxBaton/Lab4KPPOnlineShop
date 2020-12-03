package com.example.logic.admin.adminClients

import com.example.logic.account.AccountController
import com.example.logic.authorization.database.ConstAccount
import com.example.logic.authorization.database.ConstClient
import com.example.logic.authorization.database.DatabaseHandler
import com.example.logic.authorization.tables.Account
import com.example.logic.authorization.tables.Client
import com.example.logic.clientAccount.ClientAccountController
import com.example.logic.logs.LogApp
import tornadofx.Controller
import java.lang.Exception
import java.sql.SQLException
import java.util.logging.Level

class AdminClientsController: Controller() {
    private val myLog = LogApp.getLogger()

    fun getListClientForAdmin(): MutableList<ClientForAdmin>? {
        val listOfClientForAdmin = mutableListOf<ClientForAdmin>()

        val listOfClient = getClientInfo()
        val listOfClientAccount = getClientAccountInfo(listOfClient)

        listOfClient.forEachIndexed { index, client ->
            val clientForAdmin = ClientForAdmin(id = client.id, number = index + 1, name = client.name,
                                                address = client.address, status = client.status,
                                                accountAmount = listOfClientAccount[index].amount)

            listOfClientForAdmin.add(clientForAdmin)
        }

        return listOfClientForAdmin
    }

    private fun getClientInfo(): List<Client> {
        val listOfClient = mutableListOf<Client>()

        val select = "SELECT * FROM ${ConstClient.CLIENT_TABLE}"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(select)
            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val client = Client(  id = resultSet.getInt(ConstClient.CLIENT_ID),
                        name = resultSet.getString(ConstClient.CLIENT_NAME),
                        address = resultSet.getString(ConstClient.CLIENT_ADDRESS),
                        status = resultSet.getString(ConstClient.CLIENT_STATUS),
                        login = resultSet.getString(ConstClient.CLIENT_LOGIN),
                        password = resultSet.getString(ConstClient.CLIENT_PASSWORD)
                )
                listOfClient.add(client)
            }

            myLog.log(Level.INFO, "AdminClientsController: Список клиентов: $listOfClient")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminClientsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminClientsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminClientsController: Exception", e)
        }

        return listOfClient
    }

    fun changeClientStatus(clientId: Int, status: String): Boolean {
        val updateStatus = "UPDATE ${ConstClient.CLIENT_TABLE} SET " +
                "${ConstClient.CLIENT_STATUS} = ? WHERE ${ConstClient.CLIENT_ID} = $clientId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().
                                                                        prepareStatement(updateStatus)
            preparedStatement.setString(1, status)
            preparedStatement.executeUpdate()

            val clientAccountInfo = getClientInfo()
            var isClientInLIst = false
            clientAccountInfo.forEach {
                if (it.id == clientId){
                    isClientInLIst = true
                    return@forEach
                }
            }
            if (!isClientInLIst) return false

            ClientAccountController.client?.status = status

            myLog.log(Level.INFO, "AdminClientsController: Статус клиента успешно изменен на '$status'")
            if (preparedStatement != null) return true
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminClientsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminClientsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminClientsController: Exception", e)
        }
        return false
    }

    private fun getClientAccountInfo(listOfClient: List<Client>): List<Account> {
        val listOfAccount = mutableListOf<Account>()

        val select = "SELECT * FROM ${ConstAccount.ACCOUNT_TABLE}"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(select)
            val resultSet = preparedStatement.executeQuery()

            var i = 0
            while (resultSet.next()) {
                val account = Account(  id = resultSet.getInt(ConstAccount.ACCOUNT_ID),
                        amount = resultSet.getInt(ConstAccount.ACCOUNT_AMOUNT),
                        client = listOfClient[i]
                )

                listOfAccount.add(account)
                i++
            }

            myLog.log(Level.INFO, "AdminClientsController: Счета клиентов - $listOfAccount")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminClientsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminClientsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminClientsController: Exception", e)
        }

        return listOfAccount
    }

}