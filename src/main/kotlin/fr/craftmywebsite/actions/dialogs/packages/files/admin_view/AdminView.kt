package fr.craftmywebsite.actions.dialogs.packages.files.admin_view

import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Packages
import fr.craftmywebsite.utils.Templates

object AdminView {
    fun generate(
        psiDirectory: PsiDirectory,
        fileName: String
    ) {
        val packageName = Packages.findPackageName(psiDirectory)

        val templateContent = Templates.getTemplateFile("/templates/package/files/view.admin.php.template")
            .replace("\${packageName}", packageName)

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileName}.admin.view.php",
            content = templateContent,
        )
    }
}