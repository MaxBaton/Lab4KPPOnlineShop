package com.example.logic.authorization.logIn

import com.example.logic.authorization.database.ConstClient
import com.example.logic.authorization.database.DatabaseHandler
import com.example.logic.authorization.tables.Client
import com.example.logic.clientAccount.ClientAccountController
import com.example.logic.logs.LogApp
import com.example.view.client.ClientAccountView
import com.example.view.client.LogInView
import com.example.view.client.SignUpFragment
import com.sun.org.apache.xpath.internal.operations.Mod
import javafx.stage.Modality
import javafx.stage.StageStyle
import tornadofx.Controller
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.sql.ResultSet
import java.sql.SQLException
import java.util.logging.Level


class LogInController: Controller() {
    private val myLog = LogApp.getLogger()

    fun signIn(login: String, password: String): ResultSet? {
        var resultSet: ResultSet? = null

        if (login.isEmpty() && password.isEmpty()) {
            myLog.log(Level.WARNING,"Пустой пароль")
        }

        if (login.isNotEmpty() && password.isNotEmpty()) {

            val select = "SELECT * FROM ${ConstClient.CLIENT_TABLE} WHERE ${ConstClient.CLIENT_LOGIN} " +
                    "= ? AND ${ConstClient.CLIENT_PASSWORD} = ?"

            try {
                val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(select)
                preparedStatement.setString(1, login)
                preparedStatement.setString(2, password)
                resultSet = preparedStatement.executeQuery()
                val data = mutableListOf<String>()

                while (resultSet.next()) {
                    //val row: ObservableList<*> = FXCollections.observableArrayList<Any>()
                    val columnCount = resultSet.metaData.columnCount
                    for (i in 1..columnCount) {
                        val row = resultSet.getString(i)
                        data.add(row)
                    }
                }

                ClientAccountController.client = Client(id = data[0].toInt(),name = data[1], address = data[2], status = data[3],
                                                        login = data[4], password = data[5])
//                ClientAccountView().openWindow(modality = Modality.NONE)

                myLog.log(Level.INFO, "LogInController: Успешный вход в аккаунт")
            }catch (e: SQLException) {
                myLog.log(Level.SEVERE, "LogInController: SQLException ", e)
            }catch (e: ClassNotFoundException) {
                myLog.log(Level.SEVERE, "LogInController: ClassNotFoundException", e)
            }catch (e: IndexOutOfBoundsException) {
                myLog.log(Level.SEVERE, "LogInController: IndexOutOfBoundsException", e)
            }catch (e: Exception) {
                myLog.log(Level.SEVERE, "LogInController: Exception", e)
            }
        }

        return resultSet
    }

    fun notRegister() {
        find<SignUpFragment>().openModal(stageStyle = StageStyle.UTILITY)
    }
}