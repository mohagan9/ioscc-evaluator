package com.automation.iosccevaluator.actions

import com.automation.iosccevaluator.dialogs.ErrorDialog
import com.automation.iosccevaluator.dialogs.ResultDialog
import com.automation.iosccevaluator.exceptions.InvalidClassChainExpressionException
import com.automation.iosccevaluator.xcui.ClassChainEvaluator
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.xml.XmlDocument
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class EvaluateAction(private val xmlDocument: XmlDocument) : AbstractAction("Evaluate") {
    var expression = ""
    init {
        putValue(DialogWrapper.DEFAULT_ACTION, true)
        putValue(DialogWrapper.FOCUSED_ACTION, true)
    }

    override fun actionPerformed(e: ActionEvent?) {
        try {
            ResultDialog(
                ClassChainEvaluator(xmlDocument.rootTag)
                    .findAllBy(expression)
                    .size
            ).show()
        } catch (e: InvalidClassChainExpressionException) {
            ErrorDialog(e.message!!).show()
        }
    }
}
