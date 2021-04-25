package com.automation.iosccevaluator.actions

import com.intellij.openapi.ui.DialogWrapper
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class EvaluateAction : AbstractAction("Evaluate") {
    init {
        putValue(DialogWrapper.DEFAULT_ACTION, true)
        putValue(DialogWrapper.FOCUSED_ACTION, true)
    }

    override fun actionPerformed(e: ActionEvent?) {
        //TODO("Not yet implemented")
    }
}