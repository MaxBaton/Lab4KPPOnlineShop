package com.example.logic.admin.adminProducts

import com.example.logic.authorization.database.ConstClient
import com.example.logic.authorization.database.ConstProduct
import com.example.logic.authorization.database.DatabaseHandler
import com.example.logic.authorization.tables.Client
import com.example.logic.authorization.tables.Product
import com.example.logic.logs.LogApp
import com.sun.org.apache.xpath.internal.operations.Bool
import tornadofx.Controller
import java.lang.Exception
import java.sql.SQLException
import java.util.logging.Level

class AdminProductsController: Controller() {
    private val myLog = LogApp.getLogger()

    fun changeNumOfProduct(productId: Int,newNumOfProduct: Int): Boolean {
        if (!isProudctIdInList(productId)) return false

        val updateAmount = "UPDATE ${ConstProduct.PRODUCT_TABLE} SET ${ConstProduct.PRODUCT_NUMBER_IN_STOCK} " +
                "= $newNumOfProduct " +
                "WHERE ${ConstProduct.PRODUCT_ID} = $productId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(updateAmount)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "AdminProductsController: Кол-во продуктов с id=$productId успешно " +
                    "изменено на $newNumOfProduct")
            if (preparedStatement != null) return true
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminProductsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminProductsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminProductsController: Exception", e)
        }
        return false
    }

    fun changeCostOfProduct(productId: Int, newCostOfProduct: Int): Boolean {
        if (!isProudctIdInList(productId)) return false
        val updateAmount = "UPDATE ${ConstProduct.PRODUCT_TABLE} SET ${ConstProduct.PRODUCT_COST} " +
                "= $newCostOfProduct " +
                "WHERE ${ConstProduct.PRODUCT_ID} = $productId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(updateAmount)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "AdminProductsController: Стоимость продукта с id=$productId " +
                    "успешно изменена на $newCostOfProduct")
            if (preparedStatement != null) return true
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminProductsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminProductsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminProductsController: Exception", e)
        }
        return false
    }

    fun createNewProduct(product: Product) {
        val insertClient = "INSERT INTO ${ConstProduct.PRODUCT_TABLE}(${ConstProduct.PRODUCT_NAME}," +
                "${ConstProduct.PRODUCT_COST},${ConstProduct.PRODUCT_COUNTRY},${ConstProduct.PRODUCT_NUMBER_IN_STOCK})" +
                "VALUES(?,?,?,?)"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(insertClient)
            preparedStatement.setString(1, product.name)
            preparedStatement.setInt(2, product.cost)
            preparedStatement.setString(3, product.country)
            preparedStatement.setInt(4, product.numberInStock)

            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "AdminProductsController: Продукт $product успешно создан")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminProductsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminProductsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminProductsController: Exception", e)
        }
    }

    fun deleteProduct(productId: Int): Boolean {
        if (!isProudctIdInList(productId)) return false
        val deleteProduct = "DELETE FROM ${ConstProduct.PRODUCT_TABLE} WHERE ${ConstProduct.PRODUCT_ID} = $productId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection()
                                                                        .prepareStatement(deleteProduct)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "AdminProductsController: Продукт с id=$productId успешно удален")
            if (preparedStatement != null) return true
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminProductsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminProductsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminProductsController: Exception", e)
        }
        return true
    }

    private fun getProductsId(): List<Int> {
        val listOfClientId = mutableListOf<Int>()
        val getProductId = "SELECT ${ConstProduct.PRODUCT_ID} FROM ${ConstProduct.PRODUCT_TABLE}"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection()
                    .prepareStatement(getProductId)
            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val columnCount = resultSet.metaData.columnCount
                for (i in 1..columnCount) {
                    val row = resultSet.getString(i).toInt()
                    listOfClientId.add(row)
                }
            }

            myLog.log(Level.INFO, "AdminProductsController: id продуктов успешно получены")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminProductsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminProductsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminProductsController: Exception", e)
        }
        return listOfClientId.toList()
    }

    private fun isProudctIdInList(productId: Int): Boolean {
        var isProudctIdInList = false
        getProductsId().forEach {
            if (it == productId) {
                isProudctIdInList = true
                return@forEach
            }
        }
        return isProudctIdInList
    }
}