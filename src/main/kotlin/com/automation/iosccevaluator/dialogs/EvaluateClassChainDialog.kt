package com.automation.iosccevaluator.dialogs

import com.automation.iosccevaluator.actions.EvaluateAction
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.*

class EvaluateClassChainDialog : DialogWrapper(true) {
    init {
        init()
        title = "Evaluate iOS Class Chain"
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        panel.add(JLabel("Enter an iOS class chain expression:"), BorderLayout.NORTH)
        panel.add(JTextField(), BorderLayout.CENTER)
        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(EvaluateAction(), cancelAction)
    }
}