package com.automation.iosccevaluator.dialogs

import com.automation.iosccevaluator.actions.EvaluateAction
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.xml.XmlDocument
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class EvaluateClassChainDialog(xmlDocument: XmlDocument) : DialogWrapper(true) {
    private val expressionField = JTextField()
    private val evaluateAction = EvaluateAction(xmlDocument)

    init {
        init()
        title = "Evaluate iOS Class Chain"
        expressionField.document.addDocumentListener(
            ExpressionFieldListener(evaluateAction)
        )
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        panel.add(JLabel("Enter an iOS class chain expression:"), BorderLayout.NORTH)
        panel.add(expressionField, BorderLayout.CENTER)
        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(evaluateAction, cancelAction)
    }

    class ExpressionFieldListener(private val evaluateAction: EvaluateAction) : DocumentListener {
        override fun insertUpdate(e: DocumentEvent?) {
            update(e)
        }

        override fun removeUpdate(e: DocumentEvent?) {
            update(e)
        }

        override fun changedUpdate(e: DocumentEvent?) {
            update(e)
        }

        private fun update(e: DocumentEvent?) {
            evaluateAction.expression =
                if (e?.document == null) ""
                else e.document.getText(0, e.document.length)
        }
    }
}
