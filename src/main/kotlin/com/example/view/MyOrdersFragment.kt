package com.example.view

import com.example.logic.authorization.tables.ClientStatus
import com.example.logic.authorization.tables.Order
import com.example.logic.authorization.tables.OrderStatus
import com.example.logic.clientAccount.ClientAccountController
import com.example.logic.myOrders.MyOrdersController
import com.example.view.client.ClientAccountView
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import tornadofx.Fragment

class MyOrdersFragment: Fragment() {
    override val root: AnchorPane by fxml("/layout/myOrders.fxml")

    private val controller: MyOrdersController by inject()

    private val tableMyOrders:    TableView<Order> by fxid()
    private val orderId:          TableColumn<Order, Int> by fxid()
    private val orderAllProducts: TableColumn<Order, String> by fxid()
    private val orderCost:        TableColumn<Order, Int> by fxid()
    private val orderDate:        TableColumn<Order, String> by fxid()
    private val orderStatus:      TableColumn<Order, String> by fxid()
    private val anchorPaneOrderId: AnchorPane by fxid()
    private val btnPayOrder: Button by fxid()
    private val textIdOrder: Text by fxid()

    companion object {
        var txtClientStatus: Text? = null
    }

    init {
        anchorPaneOrderId.isVisible = false

        val listOfOrders = controller.getListOfOrders()!!
        displayOfOrders(listOfOrders)

        tableMyOrders.setOnMouseClicked {
            val index =  tableMyOrders.selectionModel.selectedIndex
            if (index != -1) {
                val text = "${listOfOrders[index].number} - ${listOfOrders[index].totalCost}$"
                textIdOrder.text = text
                anchorPaneOrderId.isVisible = index != -1
            }
        }

        btnPayOrder.setOnMouseClicked {
            if (ClientAccountController.client!!.status == ClientStatus.IN_BLACK_LIST.satusName) {
                textIdOrder.text = "Вы в ч.с."
            }else {
                val index = tableMyOrders.selectionModel.selectedIndex
                val totalCost = orderCost.getCellData(index).toInt()
                val orderId = listOfOrders[index].id
                val orderStatusString = orderStatus.getCellData(index).toString()

                if (orderStatusString != OrderStatus.PAID.statusName) {
                    controller.payOrder(totalCost, orderId)
                        listOfOrders[index].status = OrderStatus.PAID.statusName
                        changeStatusOrderInTable(listOfOrders, index)

                        anchorPaneOrderId.isVisible = false
                }else {
                    textIdOrder.text = "Товар уже оплачен"
                }
            }

            if (txtClientStatus != null) {
                val clientStatusText = txtClientStatus!!.text.substringAfter("- ")
                val currentStatus = ClientAccountController.client!!.status

                if (clientStatusText != currentStatus) {
                    ClientAccountController().setStatus(txtClientStatus!!)
                }
            }
        }
    }

    private fun changeStatusOrderInTable(listOfOrders: List<Order>, index: Int) {
        tableMyOrders.items = FXCollections.observableArrayList(listOfOrders)
        tableMyOrders.refresh()
    }

    private fun displayOfOrders(listOfOrders: List<Order>) {
        val fxList = FXCollections.observableArrayList(listOfOrders)

        orderId.cellValueFactory          = PropertyValueFactory("number")
        orderDate.cellValueFactory        = PropertyValueFactory("date")
        orderCost.cellValueFactory        = PropertyValueFactory("totalCost")
        orderStatus.cellValueFactory      = PropertyValueFactory("status")
        orderAllProducts.cellValueFactory = PropertyValueFactory("allProducts")

        tableMyOrders.items = fxList
    }
}