package fr.craftmywebsite.actions.dialogs.packages.files

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


class InterfaceDialog : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val view = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE) ?: return
        val psiDirectory = PsiManager.getInstance(project).findDirectory(view) ?: return

        var fileName = Messages.showInputDialog(
            project,
            "Enter the interface name:",
            "New Interface",
            Messages.getQuestionIcon()
        )?.capitalize() ?: return

        fileName = if (fileName.startsWith("I")) fileName else "I$fileName"

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.INTERFACE)

        val templateContent = Templates.getTemplateFile("/templates/files/interface.php.template")
            .replace("\${interfaceName}", fileName)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)

        Files.createFile(psiDirectory, "${fileName}.php", templateContent)
    }

    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = Files.isInAllowedFolder(virtualFile, PackageTypes.INTERFACE)
        event.presentation.icon = ExtensionIcons.action
    }

}
