package com.example.logic.makeOrder

import com.example.logic.authorization.database.ConstProduct
import com.example.logic.authorization.database.DatabaseHandler
import com.example.logic.authorization.tables.Product
import com.example.logic.logs.LogApp
import tornadofx.Controller
import java.lang.Exception
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level

class MakeOrderController: Controller() {
    private val myLog = LogApp.getLogger()

    companion object {
        var listOfProduct: MutableList<Product>? = null
    }

    fun getListOfProducts(): MutableList<Product> {
        val _listOfProducts = mutableListOf<Product>()

        val select = "SELECT * FROM ${ConstProduct.PRODUCT_TABLE}"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(select)
            val resultSet = preparedStatement.executeQuery()

            var i = 1
            while (resultSet.next()) {
                val product = Product(  id = resultSet.getInt(ConstProduct.PRODUCT_ID), number = i,
                        name = resultSet.getString(ConstProduct.PRODUCT_NAME),
                        cost = resultSet.getInt(ConstProduct.PRODUCT_COST),
                        country = resultSet.getString(ConstProduct.PRODUCT_COUNTRY),
                        numberInStock = resultSet.getInt(ConstProduct.PRODUCT_NUMBER_IN_STOCK)
                )
                _listOfProducts.add(product)
                i++
            }

            myLog.log(Level.INFO, "MakeOrderController: Список продуктов $_listOfProducts получен")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "MakeOrderController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "MakeOrderController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "MakeOrderController: Exception", e)
        }

        listOfProduct = _listOfProducts
        return _listOfProducts
    }

    fun changeListOfProducts(idNumberChangedProduct: MutableMap<Int, Int>) {
           listOfProduct!!.forEach {
               if (idNumberChangedProduct[it.id] != null) {
                   it.numberInStock  -= idNumberChangedProduct[it.id]!!
                   updateInDatabase(it.id, it.numberInStock)
               }
           }
    }

    private fun updateInDatabase(id: Int, numberInStock: Int) {
        try {
            val updateNumberInStock = "UPDATE ${ConstProduct.PRODUCT_TABLE} SET ${ConstProduct.PRODUCT_NUMBER_IN_STOCK} = " +
                    "$numberInStock WHERE ${ConstProduct.PRODUCT_ID} = $id"

            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(updateNumberInStock)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "MakeOrderController: Стало $numberInStock в продукте с id=$id")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "MakeOrderController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "MakeOrderController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "MakeOrderController: Exception", e)
        }
    }

    fun getCurrentDate(): String {
        val date = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

        return date.format(formatter)
    }
}