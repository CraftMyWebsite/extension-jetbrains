package fr.craftmywebsite.actions.dialogs.packages.files.publics

import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object Public {
    fun generate(
        psiDirectory: PsiDirectory,
        fileName: String
    ) {
        val packageName = Packages.findPackageName(psiDirectory)

        val templateContent = Templates.getTemplateFile("/templates/package/files/public.view.php.template")
            .replace("\${packageName}", packageName)

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileName}.view.php",
            content = templateContent,
        )
    }
}