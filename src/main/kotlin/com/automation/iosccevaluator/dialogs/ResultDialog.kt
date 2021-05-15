package com.automation.iosccevaluator.dialogs

import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class ResultDialog(private val resultCount: Int) : DialogWrapper(false) {
    init {
        init()
        title = "Results"
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        val matchSuffix = if (resultCount == 1) "" else "es"
        panel.add(JLabel("Found $resultCount match$matchSuffix"), BorderLayout.NORTH)
        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(okAction)
    }
}
