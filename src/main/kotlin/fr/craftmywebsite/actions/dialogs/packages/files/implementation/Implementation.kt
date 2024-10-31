package fr.craftmywebsite.actions.dialogs.packages.files.implementation

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Directory
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Implementation {
    fun generate(
        implementedPackage: String,
        project: Project,
        interfaceDirectory: PsiDirectory,
        interfaceFullDirectory: PsiDirectory,
        interfaceName: String
    ) {
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
            if (interfaceName.startsWith("I")) "${interfaceName.substring(1)}Implementation"
            else "${interfaceName}Implementation"

        val namespace = Packages.buildNamespace(implementationDirectory, PackageTypes.IMPLEMENTATION)
        val interfaceNamespace = Packages.buildNamespace(interfaceFullDirectory, PackageTypes.INTERFACE)

        //Generate implementation file content
        val templateContent = Templates.getTemplateFile("/templates/package/files/implementation.php.template")
            .replace("\${namespace}", namespace)
            .replace("\${implementationName}", implementationName)
            .replace("\${interfaceNamespace}", interfaceNamespace)
            .replace("\${interfaceName}", interfaceName)
            .replace("\${packageName}", implementedPackage)

        Files.createFile(
            directory = implementationDirectory,
            fileName = "${implementationName}.php",
            content = templateContent,
        )
    }
}