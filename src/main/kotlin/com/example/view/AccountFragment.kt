package com.example.view

import com.example.logic.account.AccountController
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import tornadofx.Fragment

class AccountFragment : Fragment() {
    companion object {
        var isFromRegisterView: Boolean = false
        var numClick = 0
    }

    override val root: AnchorPane by fxml("/layout/account.fxml")

    private val controller: AccountController by inject()

    private val txtAccountSum: Text by fxid()
    private val btnTopUpAccount: Button by fxid()
    private val textFieldSum: TextField by fxid()
    private val textError: Text by fxid()

    init {
        if (isFromRegisterView && numClick <= 1)  {
            controller.createAccount()
            controller.setAccountId()
        }

        if (!isFromRegisterView && numClick <= 1) controller.loadAccount()

        controller.setInitialAmount(txtAccountSum)

        btnTopUpAccount.setOnMouseClicked {
            if (textFieldSum.text.toString().isNotEmpty()) {
                if (textFieldSum.text.toLong() < 10_000) {
                    textError.isVisible = false
                    controller.setNewAmount(txtAccountSum, textFieldSum.text.toString().toInt())
                    textFieldSum.clear()
                }else {
                    textError.isVisible = true
                    textFieldSum.clear()
                }
            }
        }
    }
}