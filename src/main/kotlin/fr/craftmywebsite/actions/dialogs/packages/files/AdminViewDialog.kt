package fr.craftmywebsite.actions.dialogs.packages.files

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiManager
import fr.craftmywebsite.icons.ExtensionIcons
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

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

        val packageName = Packages.findPackageName(psiDirectory)

        val templateContent = Templates.getTemplateFile("/templates/files/view.admin.php.template")
            .replace("\${packageName}", packageName)

        Files.createFile(psiDirectory, "${fileName}.admin.view.php", templateContent)
    }

    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = Files.isInAllowedFolder(virtualFile, PackageTypes.VIEW)
        event.presentation.icon = ExtensionIcons.action
    }

}
