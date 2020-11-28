package com.example.view.admin

import com.example.logic.admin.adminProducts.AdminProductsController
import com.example.logic.authorization.tables.Order
import com.example.logic.authorization.tables.Product
import com.example.logic.makeOrder.MakeOrderController
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import tornadofx.Fragment
import kotlin.math.cos

class AdminProductsFragment: Fragment() {
    override val root: AnchorPane by fxml("/layout/admin/adminProducts.fxml")

    private val controller: AdminProductsController by inject()

    private val tableOfProducts: TableView<Product> by fxid()
    private val productId: TableColumn<Product, Int> by fxid()
    private val productName: TableColumn<Product, String> by fxid()
    private val productCost: TableColumn<Product, Int> by fxid()
    private val productCountry: TableColumn<Product, String> by fxid()
    private val productNumInStock: TableColumn<Product, Int> by fxid()

    //edit product anchorPane
    private val anchorPaneChangeProduct: AnchorPane by fxid()
    private val textFieldNumOfProduct: TextField by fxid()
    private val textFieldCostOfProduct: TextField by fxid()
    private val btnDeleteProduct: Button by fxid()
    private val btnChangeProduct: Button by fxid()
    private val btnCreateNewProduct: Button by fxid()

    //create product anchorPane
    private val anchorPaneCreateNewProduct: AnchorPane by fxid()
    private val textFieldNameNewProduct: TextField by fxid()
    private val textFieldCountryNewProduct: TextField by fxid()
    private val textFieldCostNewProduct: TextField by fxid()
    private val textFieldNumNewProduct: TextField by fxid()
    private val btnConfirmCreating: Button by fxid()



    init {
        anchorPaneChangeProduct.isVisible = false
        anchorPaneCreateNewProduct.isVisible = false

        val listOfProducts = MakeOrderController().getListOfProducts()
        displayListOfProducts(listOfProducts)

        tableOfProducts.setOnMouseClicked {
            val index = tableOfProducts.selectionModel.selectedIndex
            if (index != -1) {
                anchorPaneChangeProduct.isVisible = true
                val numOfProduct = productNumInStock.getCellData(index).toInt()
                val costOfProduct = productCost.getCellData(index).toInt()
                textFieldNumOfProduct.text = "$numOfProduct шт."
                textFieldCostOfProduct.text = "$costOfProduct $"
            }
        }

        btnChangeProduct.setOnMouseClicked {
            val index = tableOfProducts.selectionModel.selectedIndex

            val productId = listOfProducts[index].id//productId.getCellData(index).toInt()
            val startNumOfProduct = productNumInStock.getCellData(index).toInt()
            val startCostOfProduct = productCost.getCellData(index).toInt()
            val numOfProduct = textFieldNumOfProduct.text.substringBefore(' ').toInt()
            val costOfProduct = textFieldCostOfProduct.text.substringBefore(' ').toInt()

            if (numOfProduct != startNumOfProduct) {
                controller.changeNumOfProduct(productId, numOfProduct)

                listOfProducts[index].numberInStock = numOfProduct
                changeProductDataInTable(listOfProducts)
            }
            if (costOfProduct != startCostOfProduct) {
                controller.changeCostOfProduct(productId, costOfProduct)

                listOfProducts[index].cost = costOfProduct
                changeProductDataInTable(listOfProducts)
            }

            anchorPaneChangeProduct.isVisible = false
        }

        btnDeleteProduct.setOnMouseClicked {
            val index = tableOfProducts.selectionModel.selectedIndex
            val productId = listOfProducts[index].id//productId.getCellData(index).toInt()

            listOfProducts.removeAt(index)
            changeProductDataInTable(listOfProducts)
            controller.deleteProduct(productId)

            anchorPaneChangeProduct.isVisible = false
        }

        btnCreateNewProduct.setOnMouseClicked {
            anchorPaneCreateNewProduct.isVisible = true
        }

        btnConfirmCreating.setOnMouseClicked {
            val productName     = textFieldNameNewProduct.text.trim()
            val productCountry  = textFieldCountryNewProduct.text.trim()
            val productCost       = textFieldCostNewProduct.text.trim().toInt()
            val productNum        = textFieldNumNewProduct.text.trim().toInt()

            if (productName.isNotEmpty() && productCountry.isNotEmpty() && productCost > 0 && productNum > 0) {
                val id = listOfProducts[listOfProducts.size - 1].id
                val number = listOfProducts.size + 1
                val newProduct = Product(number = number, id = id + 1,name = productName, cost = productCost, country = productCountry,
                                        numberInStock = productNum)

                listOfProducts.add(newProduct)
                controller.createNewProduct(product = newProduct)
                changeProductDataInTable(listOfProducts)
            }

            textFieldNameNewProduct.clear()
            textFieldCountryNewProduct.clear()
            textFieldCostNewProduct.clear()
            textFieldNumNewProduct.clear()
            anchorPaneCreateNewProduct.isVisible = false
        }
    }

    private fun changeProductDataInTable(listOfProducts: List<Product>) {
        tableOfProducts.items = FXCollections.observableArrayList(listOfProducts)
        tableOfProducts.refresh()
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