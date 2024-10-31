package fr.craftmywebsite.actions.dialogs.packages.files.entity

import ai.grazie.utils.capitalize
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Entity {
    fun generate(psiDirectory: PsiDirectory, fileName: String) {
        val fileNameCleaned = (if (fileName.endsWith("Entity")) fileName else "${fileName}Entity").capitalize()

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.ENTITY)

        val templateContent = Templates.getTemplateFile("/templates/package/files/entity.php.template")
            .replace("\${entityName}", fileNameCleaned)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileNameCleaned}.php",
            content = templateContent,
        )
    }
}