package com.rudolf.tr.bigdecimalplugin

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.DialogPanel


import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.panel

class FactorizeAction1 : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(event: AnActionEvent) {
        // Using the event, create and show a dialog
        val currentProject = event.project
        val selectedText = event.getData(CommonDataKeys.EDITOR)?.selectionModel?.selectedText
        val inactiveSelected = event.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE)?.selectionModel?.selectedText
        val selectedElement = event.getData(CommonDataKeys.NAVIGATABLE).toString()

        val message = """
            event.presentation.text + " Selected!"\n
            selectedText=${selectedText}\n
            inactiveSelected=${inactiveSelected}\n
            selectedElement=${selectedElement}\n
        """.trimIndent()
        panel {
            row("Row1 label:") {
                textField()
                label("Some text")
            }

            row("Row2:") {
                label("This text is aligned with previous row")
            }

            row("Row3:") {
                label("Rows 3 and 4 are in common parent grid")
                textField()
            }.layout(RowLayout.PARENT_GRID)

            row("Row4:") {
                textField()
                label("Rows 3 and 4 are in common parent grid")
            }.layout(RowLayout.PARENT_GRID)
        }
        Messages.showMessageDialog(
                currentProject,
                message,
                event.presentation.description,
                Messages.getInformationIcon())
    }

    override fun update(e: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}
