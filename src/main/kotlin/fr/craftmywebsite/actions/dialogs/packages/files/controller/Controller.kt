package fr.craftmywebsite.actions.dialogs.packages.files.controller

import ai.grazie.utils.capitalize
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Controller {
    fun generate(psiDirectory: PsiDirectory, fileName: String) {
        val fileNameCleaned = (if (fileName.endsWith("Controller")) fileName else "${fileName}Controller").capitalize()

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.CONTROLLER)

        val templateContent = Templates.getTemplateFile("/templates/package/files/controller.php.template")
            .replace("\${controllerName}", fileNameCleaned)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)
            .replace("\${packageNameLower}", packageName.lowercase())

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileNameCleaned}.php",
            content = templateContent,
        )
    }
}