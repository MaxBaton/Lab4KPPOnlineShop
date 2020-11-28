package com.example.view.admin

import com.example.logic.admin.adminClients.AdminClientsController
import com.example.logic.admin.adminClients.ClientForAdmin
import com.example.logic.authorization.tables.ClientStatus
import com.example.logic.authorization.tables.Product
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import tornadofx.Fragment

class AdminClientsFragment: Fragment() {
    override val root: AnchorPane by fxml("/layout/admin/adminClients.fxml")

    private val controller: AdminClientsController by inject()

    private val tableOfClients: TableView<ClientForAdmin> by fxid()
    private val clientId: TableColumn<ClientForAdmin, Int> by fxid()
    private val clientName: TableColumn<ClientForAdmin, String> by fxid()
    private val clientAddress: TableColumn<ClientForAdmin, String> by fxid()
    private val clientStatus: TableColumn<ClientForAdmin, String> by fxid()
    private val clientAmount: TableColumn<ClientForAdmin, Int> by fxid()

    //add client to back list anchorPane
    private val anchorPaneAddClientToBackList: AnchorPane by fxid()
    private val textAreaClientInformation: TextArea by fxid()
    private val btnAddToBackList: Button by fxid()
    private val btnReestablishStatus: Button by fxid()

    init {
        anchorPaneAddClientToBackList.isVisible = false

        val listOfClientForAdmin = controller.getListClientForAdmin()
        displayListOfClientForAdmin(listOfClientForAdmin)

        tableOfClients.setOnMouseClicked {
            val index = tableOfClients.selectionModel.selectedIndex
            if (index != -1) {
                anchorPaneAddClientToBackList.isVisible = true

                val nameOfClient = clientName.getCellData(index).toString()
                val clientAmount = clientAmount.getCellData(index).toInt()
                val text = "$nameOfClient - $clientAmount$"
                textAreaClientInformation.text = text

                if (listOfClientForAdmin[index].status == ClientStatus.IN_BLACK_LIST.satusName) {
                    btnAddToBackList.isVisible = false
                    btnReestablishStatus.isVisible = true
                }else {
                    btnAddToBackList.isVisible = true
                    btnReestablishStatus.isVisible = false
                }
            }
        }

        btnAddToBackList.setOnMouseClicked {
            val index = tableOfClients.selectionModel.selectedIndex


            val clientId = listOfClientForAdmin[index].id
            controller.changeClientStatus(clientId, ClientStatus.IN_BLACK_LIST.satusName)
            listOfClientForAdmin[index].status = ClientStatus.IN_BLACK_LIST.satusName
            changeClientStatusInTable(listOfClientForAdmin)

            anchorPaneAddClientToBackList.isVisible = false
        }

        btnReestablishStatus.setOnMouseClicked {
            val index = tableOfClients.selectionModel.selectedIndex

            val clientId = listOfClientForAdmin[index].id
            controller.changeClientStatus(clientId, ClientStatus.USUAL.satusName)
            listOfClientForAdmin[index].status = ClientStatus.USUAL.satusName
            changeClientStatusInTable(listOfClientForAdmin)

            anchorPaneAddClientToBackList.isVisible = false
        }
    }

    private fun displayListOfClientForAdmin(listOfClientForAdmin: List<ClientForAdmin>) {
        val fxList = FXCollections.observableArrayList(listOfClientForAdmin)

        clientId.cellValueFactory            = PropertyValueFactory("number")
        clientName.cellValueFactory          = PropertyValueFactory("name")
        clientAddress.cellValueFactory       = PropertyValueFactory("address")
        clientStatus.cellValueFactory        = PropertyValueFactory("status")
        clientAmount.cellValueFactory        = PropertyValueFactory("accountAmount")

        tableOfClients.items = fxList
    }

    private fun changeClientStatusInTable(listOfClients: List<ClientForAdmin>) {
        tableOfClients.items = FXCollections.observableArrayList(listOfClients)
        tableOfClients.refresh()
    }
}