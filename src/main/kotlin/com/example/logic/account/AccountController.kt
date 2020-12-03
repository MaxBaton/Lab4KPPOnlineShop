package com.example.logic.account

import com.example.logic.authorization.database.ConstAccount
import com.example.logic.authorization.database.ConstClient
import com.example.logic.authorization.database.DatabaseHandler
import com.example.logic.authorization.tables.Account
import com.example.logic.clientAccount.ClientAccountController
import com.example.logic.logs.LogApp
import com.example.view.client.ClientAccountView
import com.example.view.client.LogInView
import javafx.scene.text.Text
import tornadofx.Controller
import java.lang.Exception
import java.sql.ResultSet
import java.sql.SQLException
import java.util.logging.Level

class AccountController: Controller() {
    private val myLog = LogApp.getLogger()

    companion object {
        var account: Account? = null
    }

    fun setInitialAmount(txtAccountSum: Text) {
        txtAccountSum.text = "Сумма на\nсчете: ${account?.amount.toString()} $"
    }

    fun setNewAmount(txtAccountSum: Text? = null, amount: Int) {
        val newAmount = if (txtAccountSum != null) account!!.amount + amount else amount

        if (txtAccountSum != null) {
            txtAccountSum.text = "Сумма на\nсчете: $newAmount $"
        }
        val accountId = account!!.id

        val updateAmount = "UPDATE ${ConstAccount.ACCOUNT_TABLE} SET ${ConstAccount.ACCOUNT_AMOUNT} = $newAmount " +
                "WHERE ${ConstAccount.ACCOUNT_ID} = $accountId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(updateAmount)
            preparedStatement.executeUpdate()

            account!!.amount = newAmount

            myLog.log(Level.INFO, "AccountController: Новый счет: ${account!!.amount}$")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AccountController: SQLException $", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AccountController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AccountController: Exception", e)
        }
    }

    fun createAccount() {
        val clientId = ClientAccountController.client!!.id

        val insertClient = "INSERT INTO ${ConstAccount.ACCOUNT_TABLE} (${ConstAccount.ACCOUNT_AMOUNT}," +
                "${ConstAccount.ACCOUNT_ID_CLIENT}) VALUES(${account!!.amount},(SELECT ${ConstClient.CLIENT_ID} FROM ${ConstClient.CLIENT_TABLE} " +
                "WHERE ${ConstClient.CLIENT_ID} = $clientId))"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(insertClient)
            preparedStatement.executeUpdate()

            account = Account(client = ClientAccountController.client!!)

            myLog.log(Level.INFO, "AccountController: Аккаунт успешно создан")
        } catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AccountController: SQLException", e)
        } catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AccountController: ClassNotFoundException", e)
        } catch (e: Exception) {
            myLog.log(Level.SEVERE, "AccountController: Exception", e)
        }
    }

    fun loadAccount(): Boolean {
        try {
            val clientId = ClientAccountController.client!!.id

            val select = "SELECT * FROM ${ConstAccount.ACCOUNT_TABLE} WHERE " +
                    "${ConstAccount.ACCOUNT_ID_CLIENT} = $clientId"

            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(select)
            val resultSet = preparedStatement.executeQuery()
            val data = mutableListOf<String>()

            while (resultSet.next()) {
                val columnCount = resultSet.metaData.columnCount
                for (i in 1..columnCount) {
                    val row = resultSet.getString(i)
                    data.add(row)
                }
            }

            account = Account(id = data[0].toInt(), amount = data[1].toInt(), client = ClientAccountController.client!!)

            myLog.log(Level.INFO, "AccountController: Текущий счет: ${account!!.amount}$")
            if (resultSet != null) return true
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AccountController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AccountController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AccountController: Exception", e)
        }
        return false
    }

    fun setAccountId() {
        try {
            var resultSet: ResultSet? = null
            val _account = account
            val clientId = ClientAccountController.client!!.id
            val select = "SELECT * FROM ${ConstAccount.ACCOUNT_TABLE} WHERE ${ConstAccount.ACCOUNT_AMOUNT} = " +
                    "${_account!!.amount} AND ${ConstAccount.ACCOUNT_ID_CLIENT} = $clientId"

            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(select)
            resultSet = preparedStatement.executeQuery()
            val data = mutableListOf<String>()

            while (resultSet.next()) {
                //val row: ObservableList<*> = FXCollections.observableArrayList<Any>()
                val columnCount = resultSet.metaData.columnCount
                for (i in 1..1) {
                    val row = resultSet.getString(i)
                    data.add(row)
                }
            }

            val id = data[0].toInt()
            account!!.id = id

            myLog.log(Level.INFO, "AccountController: id аккаунта успешно создан")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AccountController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AccountController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AccountController: Exception", e)
        }
    }
}