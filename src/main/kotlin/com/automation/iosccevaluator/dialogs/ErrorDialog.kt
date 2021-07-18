package com.automation.iosccevaluator.dialogs

import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.*

class ErrorDialog(private val errorMessage: String) : DialogWrapper(false) {
    init {
        init()
        title = "Error"
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        val label = JLabel(errorMessage)
        label.icon = ImageIcon(javaClass.classLoader.getResource("/icons/error.png"))
        panel.add(label, BorderLayout.NORTH)
        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(okAction)
    }
}
