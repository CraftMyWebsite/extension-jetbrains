package fr.craftmywebsite.actions.dialogs.packages.files.implementation

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.isFile
import fr.craftmywebsite.icons.ExtensionIcons
import fr.craftmywebsite.utils.Directory
import fr.craftmywebsite.utils.Packages


class ImplementationDialog : AnAction() {

    private var interfaceName: String? = null

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val interfacePath = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE) ?: return
        val interfaceDirectory = Packages.findPackageDirectory(interfacePath, project) ?: return
        val interfaceFullDirectory = Directory.getDirectory(interfacePath, project) ?: return

        if (interfaceName == null) return

        //Get Implemented package name
        val implementedPackage = Messages.showInputDialog(
            project,
            "Enter the implemented package name:",
            "New Implementation - $interfaceName",
            Messages.getQuestionIcon(),
        ) ?: return

        Implementation.generate(
            implementedPackage,
            project,
            interfaceDirectory,
            interfaceFullDirectory,
            interfaceName!!
        )
    }


    override fun update(event: AnActionEvent) {
        val virtualFile = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)

        event.presentation.isEnabledAndVisible =
            virtualFile != null && virtualFile.isFile && virtualFile.name.startsWith('I') && virtualFile.fileType.name == "PHP"

        event.presentation.icon = ExtensionIcons.action

        if (event.presentation.isEnabledAndVisible) {
            interfaceName = virtualFile?.nameWithoutExtension
        }
    }

}
