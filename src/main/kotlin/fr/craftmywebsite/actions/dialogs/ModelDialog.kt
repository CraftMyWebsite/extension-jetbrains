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

class ModelDialog : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val view = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE) ?: return
        val psiDirectory = PsiManager.getInstance(project).findDirectory(view) ?: return

        val fileName = Messages.showInputDialog(
            project,
            "Enter the model name:",
            "New Model",
            Messages.getQuestionIcon()
        )?.capitalize() ?: return

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.MODEL)

        val templateContent = Templates.getTemplateFile("/templates/files/model.php.template")
            .replace("\${modelName}", fileName)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)

        Files.createFile(psiDirectory, "${fileName}Model.php", templateContent)
    }

    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = virtualFile != null && virtualFile.isDirectory
        event.presentation.icon = ExtensionIcons.action
    }

}
