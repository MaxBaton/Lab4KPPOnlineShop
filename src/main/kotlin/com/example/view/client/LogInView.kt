package com.example.view.client

import com.example.logic.authorization.database.Admin
import com.example.logic.authorization.logIn.LogInController
import com.example.view.admin.AdminAccountView
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.StageStyle
import tornadofx.*

class LogInView : View("online_shop") {
    override val root:AnchorPane by fxml("/layout/main.fxml")

    private val controller: LogInController by inject()

    private val txtFieldLogin: TextField by fxid("textFieldLogin")
    private val txtFieldPassword: PasswordField by fxid("textFieldPassword")
    private val btnSignIn: Button by fxid("btnSignIn")
    private val txtNotRegister: Text by fxid("txtNotRegistered")

    init {
        btnSignIn.setOnMouseClicked {
            val login = txtFieldLogin.text.trim()
            val password = txtFieldPassword.text.trim()
            if (password == Admin.PASSWORD && login == Admin.LOGIN) {
                AdminAccountView().openWindow(modality = Modality.NONE)
                txtFieldLogin.clear()
                txtFieldPassword.clear()
            }else {
                controller.signIn(login = login, password = password)
                txtFieldLogin.clear()
                txtFieldPassword.clear()
                ClientAccountView().openWindow(modality = Modality.NONE)
            }
        }
        txtNotRegister.setOnMouseClicked {
            if (txtFieldLogin.text.trim().isNotEmpty() || txtFieldPassword.text.trim().isNotEmpty()) {
                txtFieldLogin.clear()
                txtFieldPassword.clear()
            }
            controller.notRegister()
        }
    }
}
