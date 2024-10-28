package fr.craftmywebsite.actions.dialogs

import ai.grazie.utils.capitalize
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

class ControllerDialog : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val view = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE) ?: return
        val psiDirectory = PsiManager.getInstance(project).findDirectory(view) ?: return

        val fileName = Messages.showInputDialog(
            project,
            "Enter the controller name:",
            "New Controller",
            Messages.getQuestionIcon()
        )?.capitalize() ?: return

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.CONTROLLER)

        val templateContent = Templates.getTemplateFile("/templates/files/controller.php.template")
            .replace("\${controllerName}", fileName)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)
            .replace("\${packageNameLower}", packageName.lowercase())

        Files.createFile(psiDirectory, "${fileName}Controller.php", templateContent)
    }

    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = virtualFile != null && virtualFile.isDirectory
        event.presentation.icon = ExtensionIcons.action
    }

}
