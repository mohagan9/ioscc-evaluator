package com.automation.iosccevaluator.actions

import com.automation.iosccevaluator.dialogs.EvaluateClassChainDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class EvaluateClassChainAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        EvaluateClassChainDialog().show()
    }
}