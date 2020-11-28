package com.example.view.client

import com.example.logic.authorization.signUp.SignUpController
import com.example.logic.authorization.tables.Client
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import tornadofx.Fragment


class SignUpFragment: Fragment() {
    override val root: AnchorPane by fxml("/layout/signUp.fxml")

    private val controller: SignUpController by inject()

    private val textFieldName: TextField by fxid()
    private val textFieldAddress: TextField by fxid()
    private val textFieldLogin: TextField by fxid()
    private val textFieldPassword: PasswordField by fxid()
    private val btnSignUp: Button by fxid()

    init {
        btnSignUp.setOnMouseClicked {
            val client = Client(name = textFieldName.text, address = textFieldAddress.text,
                    login = textFieldLogin.text.trim(), password = textFieldPassword.text.trim())

            if (controller.signUp(client)) {
                ClientAccountView(isFromRegisterView = true).openWindow(modality = Modality.NONE)
                this.close()
            }else {
                val dialog = createWarningDialog()
                dialog.show()
            }
        }
    }

    private fun createWarningDialog(): Stage {
        val dialog = Stage()
        dialog.initModality(Modality.APPLICATION_MODAL)
        dialog.initOwner(primaryStage)
        val dialogVbox = VBox(20.0)
        val text = Text("Такой пароль и \nлогин уже используются!!!")
        text.font = Font.font("Verdana", 30.0)
        dialogVbox.children.add(text)
        val dialogScene = Scene(dialogVbox, 423.0, 87.0)
        dialog.scene = dialogScene
        return dialog
    }
}