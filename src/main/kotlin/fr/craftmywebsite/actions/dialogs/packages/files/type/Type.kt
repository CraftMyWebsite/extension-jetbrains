package fr.craftmywebsite.actions.dialogs.packages.files.type

import ai.grazie.utils.capitalize
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Type {
    fun generate(psiDirectory: PsiDirectory, fileName: String) {
        val fileNameCleaned = fileName.capitalize()

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.TYPE)

        val templateContent = Templates.getTemplateFile("/templates/package/files/type.php.template")
            .replace("\${typeName}", fileNameCleaned)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)

        Files.createFile(psiDirectory, "${fileNameCleaned}.php", templateContent)
    }
}