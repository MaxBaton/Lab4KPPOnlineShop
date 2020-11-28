package com.example.logic.admin.adminProducts

import com.example.logic.authorization.database.ConstClient
import com.example.logic.authorization.database.ConstProduct
import com.example.logic.authorization.database.DatabaseHandler
import com.example.logic.authorization.tables.Product
import com.example.logic.clientAccount.ClientAccountController
import com.example.logic.logs.LogApp
import tornadofx.Controller
import java.lang.Exception
import java.sql.SQLException
import java.util.logging.Level

class AdminProductsController: Controller() {
    private val myLog = LogApp.getLogger()

    fun changeNumOfProduct(productId: Int,newNumOfProduct: Int) {
        val updateAmount = "UPDATE ${ConstProduct.PRODUCT_TABLE} SET ${ConstProduct.PRODUCT_NUMBER_IN_STOCK} " +
                "= $newNumOfProduct " +
                "WHERE ${ConstProduct.PRODUCT_ID} = $productId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(updateAmount)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "AdminProductsController: Кол-во продуктов с id=$productId успешно " +
                    "изменено на $newNumOfProduct")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminProductsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminProductsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminProductsController: Exception", e)
        }
    }

    fun changeCostOfProduct(productId: Int, newCostOfProduct: Int) {
        val updateAmount = "UPDATE ${ConstProduct.PRODUCT_TABLE} SET ${ConstProduct.PRODUCT_COST} " +
                "= $newCostOfProduct " +
                "WHERE ${ConstProduct.PRODUCT_ID} = $productId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection().prepareStatement(updateAmount)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "AdminProductsController: Стоимость продукта с id=$productId " +
                    "успешно изменена на $newCostOfProduct")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminProductsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminProductsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminProductsController: Exception", e)
        }
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

    fun deleteProduct(productId: Int) {
        val deleteProduct = "DELETE FROM ${ConstProduct.PRODUCT_TABLE} WHERE ${ConstProduct.PRODUCT_ID} = $productId"

        try {
            val preparedStatement = DatabaseHandler().getDbConnection()
                                                                        .prepareStatement(deleteProduct)
            preparedStatement.executeUpdate()

            myLog.log(Level.INFO, "AdminProductsController: Продукт с id=$productId успешно удален")
        }catch (e: SQLException) {
            myLog.log(Level.SEVERE, "AdminProductsController: SQLException", e)
        }catch (e: ClassNotFoundException) {
            myLog.log(Level.SEVERE, "AdminProductsController: ClassNotFoundException", e)
        }catch (e: Exception) {
            myLog.log(Level.SEVERE, "AdminProductsController: Exception", e)
        }
    }
}