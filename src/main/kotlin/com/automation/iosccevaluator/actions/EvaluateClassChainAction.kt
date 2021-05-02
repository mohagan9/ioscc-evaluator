package com.automation.iosccevaluator.actions

import com.automation.iosccevaluator.dialogs.EvaluateClassChainDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.xml.XmlFile

class EvaluateClassChainAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val xmlDoc = (e.getData(CommonDataKeys.PSI_FILE) as XmlFile).document
        if (xmlDoc != null)
            EvaluateClassChainDialog(xmlDoc).show()
    }
}