package com.example.logic.myOrders

import com.example.logic.account.AccountController
import com.example.logic.authorization.database.*
import com.example.logic.authorization.tables.Order
import com.example.logic.authorization.tables.OrderStatus
import com.example.logic.clientAccount.ClientAccountController
import com.example.logic.logs.LogApp
import com.example.view.AccountFragment
import tornadofx.Controller
import java.lang.Exception
import java.sql.SQLException
import java.util.logging.Level

class MyOrdersController: Controller() {
    private val myLog = LogApp.getLogger()

    companion object {
        var order: Order? = null
        var idNumberChangedProduct: Map<Int, Int>? = null
    }

    fun getListOfOrders(): List<Order> {
        val listOfOrders = mutableListOf<Order>()

        val select = "SELECT * FROM ${ConstOrder.ORDER_TABLE} WHERE ${ConstOrder.ORDER_ID_CLIENT} " +
                        "= ${ClientAccountController.client!!.id}"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(select)
            val resultSet = preparedStatement.executeQuery()

            var i = 1
            while (resultSet.next()) {
                val order = Order( id = resultSet.getInt(ConstOrder.ORDER_ID), number = i,
                        date = resultSet.getString(ConstOrder.ORDER_DATE),
                        totalCost = resultSet.getInt(ConstOrder.ORDER_COST),
                        allProducts = resultSet.getString(ConstOrder.ORDER_ALL_PRODUCTS),
                        status = resultSet.getString(ConstOrder.ORDER_STATUS))

                listOfOrders.add(order)
                i++
            }

            myLog.log(Level.INFO, "MyOrdersController: Список заказов: $listOfOrders")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "MyOrdersController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "MyOrdersController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "MyOrdersController: Exception", e)
        }

        return listOfOrders
    }

    fun createOrderInDatabase() {
        val clientId = ClientAccountController.client!!.id

        val insertOrder = "INSERT INTO ${ConstOrder.ORDER_TABLE} (${ConstOrder.ORDER_DATE}," +
                "${ConstOrder.ORDER_COST},${ConstOrder.ORDER_STATUS},${ConstOrder.ORDER_ID_CLIENT}," +
                "${ConstOrder.ORDER_ALL_PRODUCTS}) VALUES(?,${order!!.totalCost},?," +
                "(SELECT ${ConstClient.CLIENT_ID} FROM ${ConstClient.CLIENT_TABLE} " +
                "WHERE ${ConstClient.CLIENT_ID} = $clientId),?)"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(insertOrder)
            preparedStatement.setString(1, order!!.date)
            preparedStatement.setString(2, order!!.status)
            preparedStatement.setString(3, order!!.allProducts)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "MyOrdersController: Заказ ${order!!.allProducts} подтвержден")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "MyOrdersController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "MyOrdersController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "MyOrdersController: Exception", e)
        }
    }

    fun payOrder(totalCost: Int, orderId: Int): Int {
        val myAmount = setNewAmount(totalCost)
        setNewProductStatus(orderId)
        return myAmount
    }

    private fun setNewProductStatus(orderId: Int) {
        val updateOrderStatus = "UPDATE ${ConstOrder.ORDER_TABLE} SET ${ConstOrder.ORDER_STATUS} = " +
                "? WHERE ${ConstOrder.ORDER_ID} = $orderId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(updateOrderStatus)
            preparedStatement.setString(1, OrderStatus.PAID.statusName)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "MyOrdersController: Заказ с id=$orderId оплачен")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "MyOrdersController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "MyOrdersController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "MyOrdersController: Exception", e)
        }
    }

    private fun setNewAmount(totalCost: Int): Int {
        if (AccountController.account == null || AccountController.account?.id == 0) {
            AccountFragment.numClick++
            AccountFragment()
        }
        val myAmount = AccountController.account!!.amount - totalCost
        AccountController().setNewAmount(amount = myAmount)
        return myAmount
    }
}