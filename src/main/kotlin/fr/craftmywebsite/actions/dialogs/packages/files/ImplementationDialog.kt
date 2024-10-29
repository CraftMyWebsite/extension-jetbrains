package fr.craftmywebsite.actions.dialogs.packages.files

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.isFile
import fr.craftmywebsite.icons.ExtensionIcons
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Directory
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates


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

        val targetPackage = Packages.findPackageDirectory(implementedPackage, project) ?: return

        //Check if Implementations folder exist, if not, create it
        if (targetPackage.findSubdirectory("Implementations") == null) {
            Directory.createDirectory(targetPackage, "Implementations")
        }

        var implementationDirectory = targetPackage.findSubdirectory("Implementations") ?: return

        val interfacePackage = Packages.findPackageName(interfaceDirectory)

        //Check if interface folder exist, if not, create it
        if (implementationDirectory.findSubdirectory(interfacePackage) == null) {
            Directory.createDirectory(implementationDirectory, interfacePackage)
        }

        implementationDirectory = implementationDirectory.findSubdirectory(interfacePackage) ?: return

        //Generate implementation file name
        val implementationName =
            if (interfaceName!!.startsWith("I")) "${interfaceName!!.substring(1)}Implementation"
            else "${interfaceName}Implementation"

        val namespace = Packages.buildNamespace(implementationDirectory, PackageTypes.IMPLEMENTATION)
        val interfaceNamespace = Packages.buildNamespace(interfaceFullDirectory, PackageTypes.INTERFACE)

        //Generate implementation file content
        val templateContent = Templates.getTemplateFile("/templates/files/implementation.php.template")
            .replace("\${namespace}", namespace)
            .replace("\${implementationName}", implementationName)
            .replace("\${interfaceNamespace}", interfaceNamespace)
            .replace("\${interfaceName}", interfaceName!!)
            .replace("\${packageName}", implementedPackage)

        Files.createFile(implementationDirectory, "${implementationName}.php", templateContent)
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
