package fr.craftmywebsite.actions.dialogs.packages.files.admin_view

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiManager
import fr.craftmywebsite.icons.ExtensionIcons
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files

class AdminViewDialog : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val view = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE) ?: return
        val psiDirectory = PsiManager.getInstance(project).findDirectory(view) ?: return

        val fileName = Messages.showInputDialog(
            project,
            "Enter the admin view name:",
            "New Admin View",
            Messages.getQuestionIcon()
        ) ?: return

        AdminView.generate(psiDirectory, fileName)
    }

    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = Files.isInAllowedPackageFolder(virtualFile, PackageTypes.VIEW)
        event.presentation.icon = ExtensionIcons.action
    }

}
