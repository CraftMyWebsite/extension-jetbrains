package fr.craftmywebsite.actions.dialogs.managers.creation

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import fr.craftmywebsite.icons.ExtensionIcons
import fr.craftmywebsite.utils.Directory

class ManagerDialog : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val view = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE) ?: return
        val psiDirectory = PsiManager.getInstance(project).findDirectory(view) ?: return

        val managerName = Messages.showInputDialog(
            project,
            "Enter the Manager name:",
            "New Manager",
            Messages.getQuestionIcon()
        ) ?: return

        val managerDirectory = Directory.createDirectory(psiDirectory, managerName) ?: return

        Manager.generateFile(managerDirectory, managerName)
    }


    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = isInAllowedFolder(virtualFile)
        event.presentation.icon = ExtensionIcons.action
    }

    private fun isInAllowedFolder(virtualFile: VirtualFile?): Boolean {
        if (virtualFile == null || !virtualFile.isDirectory) return false

        var currentDirectory = virtualFile

        while (currentDirectory != null) {
            if (currentDirectory.name == "Manager") return true
            currentDirectory = currentDirectory.parent
        }

        return false
    }
}