package fr.craftmywebsite.actions.dialogs.packages.files.interfaces

import ai.grazie.utils.capitalize
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Interface {
    fun generate(psiDirectory: PsiDirectory, fileName: String) {
        val fileNameCleaned = (if (fileName.startsWith("I")) fileName else "I$fileName").capitalize()

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.INTERFACE)

        val templateContent = Templates.getTemplateFile("/templates/package/files/interface.php.template")
            .replace("\${interfaceName}", fileNameCleaned)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileNameCleaned}.php",
            content = templateContent,
        )
    }
}