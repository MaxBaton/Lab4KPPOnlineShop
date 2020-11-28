package com.example.view.client

import com.example.logic.clientAccount.ClientAccountController
import com.example.view.AccountFragment
import com.example.view.MakeOrderFragment
import com.example.view.MyOrdersFragment
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import javafx.stage.StageStyle
import tornadofx.View

class ClientAccountView(isFromRegisterView: Boolean = false): View("Аккаунт") {
    override val root: AnchorPane by fxml("/layout/clientAccount.fxml")

    private val controller: ClientAccountController by inject()

    private val btnMakeOrder: Button by fxid()
    private val btnAccount: Button by fxid()
    private val btnMyOrders: Button by fxid()
    private val txtClientStatus: Text by fxid()

    init {
        if (isFromRegisterView) {
            controller.setClientId()
            AccountFragment.isFromRegisterView = isFromRegisterView
        }

        controller.setStatus(txtClientStatus)

        btnAccount.setOnMouseClicked {
            AccountFragment.numClick++
            find<AccountFragment>().openModal(stageStyle = StageStyle.UTILITY)
        }

        btnMakeOrder.setOnMouseClicked {
            find<MakeOrderFragment>().openModal(stageStyle = StageStyle.UTILITY)
        }

        btnMyOrders.setOnMouseClicked {
            MyOrdersFragment.txtClientStatus = txtClientStatus
            find<MyOrdersFragment>().openModal(stageStyle = StageStyle.UTILITY)
        }
    }
}