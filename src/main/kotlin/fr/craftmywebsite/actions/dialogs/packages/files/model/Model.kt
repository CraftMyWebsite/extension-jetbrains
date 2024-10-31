package fr.craftmywebsite.actions.dialogs.packages.files.model

import ai.grazie.utils.capitalize
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Model {
    fun generate(psiDirectory: PsiDirectory, fileName: String) {
        val fileNameCleaned = (if (fileName.endsWith("Model")) fileName else "${fileName}Model").capitalize()

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.MODEL)

        val templateContent = Templates.getTemplateFile("/templates/package/files/model.php.template")
            .replace("\${modelName}", fileName)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileName}.php",
            content = templateContent,
        )
    }
}