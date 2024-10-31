package fr.craftmywebsite.actions.dialogs.packages.files.event

import ai.grazie.utils.capitalize
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Event {
    fun generate(psiDirectory: PsiDirectory, fileName: String) {
        val fileNameCleaned = (if (fileName.endsWith("Event")) fileName else "${fileName}Event").capitalize()

        val packageName = Packages.findPackageName(psiDirectory)
        val namespace = Packages.buildNamespace(psiDirectory, PackageTypes.EVENT)

        val templateContent = Templates.getTemplateFile("/templates/package/files/event.php.template")
            .replace("\${eventName}", fileNameCleaned)
            .replace("\${namespace}", namespace)
            .replace("\${packageName}", packageName)

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileNameCleaned}.php",
            content = templateContent,
        )
    }
}