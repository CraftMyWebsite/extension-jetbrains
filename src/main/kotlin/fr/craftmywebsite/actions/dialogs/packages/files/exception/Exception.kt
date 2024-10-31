package fr.craftmywebsite.actions.dialogs.packages.files.exception

import ai.grazie.utils.capitalize
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Exception {
    fun generate(psiDirectory: PsiDirectory, fileName: String) {
        val fileNameCleaned = (if (fileName.endsWith("Exception")) fileName else "${fileName}Exception").capitalize()

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.EXCEPTION)

        val templateContent = Templates.getTemplateFile("/templates/package/files/exception.php.template")
            .replace("\${exceptionName}", fileNameCleaned)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileNameCleaned}.php",
            content = templateContent,
        )
    }
}