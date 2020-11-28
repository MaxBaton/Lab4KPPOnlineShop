package com.example.view.admin

import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.stage.StageStyle
import tornadofx.View

class AdminAccountView: View("Аккаунт") {
    override val root: AnchorPane by fxml("/layout/admin/adminAccount.fxml")

    private val btnClients: Button by fxid()
    private val btnProducts: Button by fxid()

    init {
        btnClients.setOnMouseClicked {
            find<AdminClientsFragment>().openModal(stageStyle = StageStyle.UTILITY)
        }

        btnProducts.setOnMouseClicked {
            find<AdminProductsFragment>().openModal(stageStyle = StageStyle.UTILITY)
        }
    }
}