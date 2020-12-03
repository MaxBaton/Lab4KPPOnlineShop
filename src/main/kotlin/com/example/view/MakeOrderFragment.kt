package com.example.view

import com.example.logic.account.AccountController
import com.example.logic.authorization.tables.Order
import com.example.logic.authorization.tables.OrderStatus
import com.example.logic.authorization.tables.Product
import com.example.logic.clientAccount.ClientAccountController
import com.example.logic.makeOrder.MakeOrderController
import com.example.logic.myOrders.MyOrdersController
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import tornadofx.Fragment

class MakeOrderFragment: Fragment() {
    override val root: AnchorPane by fxml("/layout/order.fxml")

    private val controller: MakeOrderController by inject()

    private val tableOfProducts: TableView<Product> by fxid()
    private val productId: TableColumn<Product, Int> by fxid()
    private val productName: TableColumn<Product, String> by fxid()
    private val productCost: TableColumn<Product, Int> by fxid()
    private val productCountry: TableColumn<Product, String> by fxid()
    private val productNumInStock: TableColumn<Product, Int> by fxid()
    private val anchorPaneMakeOrder: AnchorPane by fxid()
    private val textFieldNumOfProduct: TextField by fxid()
    private val textAreaMyOrder: TextArea by fxid()
    private val btnMakeOrder: Button by fxid()
    private val btnCancelOrder: Button by fxid()
    private val btnConfirmOrder: Button by fxid()
    private var totalSum = 0

    private val idNumberChangedProduct = mutableMapOf<Int, Int>()

    init {
        anchorPaneMakeOrder.isVisible = false

        val listOfProducts = controller.getListOfProducts()!!
        displayListOfProducts(listOfProducts)

        tableOfProducts.setOnMouseClicked {
            val index = tableOfProducts.selectionModel.selectedIndex
            anchorPaneMakeOrder.isVisible = index != -1
        }

        btnMakeOrder.setOnMouseClicked {
            val index = tableOfProducts.selectionModel.selectedIndex
            val productName = productName.getCellData(index).toString()
            val productId     = listOfProducts[index].id
            var productCost   = productCost.getCellData(index).toInt()

            if (textFieldNumOfProduct.text.trim().isNotEmpty()) {
                val numOfOrderedProduct = textFieldNumOfProduct.text.toInt()
                productCost *= numOfOrderedProduct
                totalSum += productCost
                textAreaMyOrder.text += createNewLineInTextAreaMyOrder(productName, numOfOrderedProduct, productCost)
                idNumberChangedProduct[productId] = numOfOrderedProduct
            }

            textFieldNumOfProduct.clear()
        }

        btnCancelOrder.setOnMouseClicked {
            totalSum = 0
            textAreaMyOrder.clear()
        }

        btnConfirmOrder.setOnMouseClicked {
            if (textAreaMyOrder.text.trim().isNotEmpty()) {
                controller.changeListOfProducts(idNumberChangedProduct)

                val date: String = controller.getCurrentDate()
                val totalString = textAreaMyOrder.text
                MyOrdersController.order = Order(date = date, totalCost = totalSum, allProducts = totalString,
                        client = ClientAccountController.client!!,
                        status = OrderStatus.ORDERED.statusName)

                MyOrdersController.idNumberChangedProduct = idNumberChangedProduct
                MyOrdersController().createOrderInDatabase()
            }

            this.close()
        }
    }

    private fun createNewLineInTextAreaMyOrder(nameOfProduct: String, numbOfProduct: Int, productCost: Int): String {
        return "$nameOfProduct - $numbOfProduct \t$productCost$ \n"
    }

    private fun displayListOfProducts(listOfProducts: List<Product>) {
        val fxList = FXCollections.observableArrayList(listOfProducts)

        productId.cellValueFactory         = PropertyValueFactory("number")
        productName.cellValueFactory       = PropertyValueFactory("name")
        productCost.cellValueFactory       = PropertyValueFactory("cost")
        productCountry.cellValueFactory    = PropertyValueFactory("country")
        productNumInStock.cellValueFactory = PropertyValueFactory("numberInStock")

        tableOfProducts.items = fxList
    }
}