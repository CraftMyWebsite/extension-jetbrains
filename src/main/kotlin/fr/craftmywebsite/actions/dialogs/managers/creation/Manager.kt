package fr.craftmywebsite.actions.dialogs.managers.creation

import ai.grazie.utils.capitalize
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.utils.Files
import fr.craftmywebsite.utils.Managers
import fr.craftmywebsite.utils.Templates

object Manager {
    fun generateFile(psiDirectory: PsiDirectory, managerName: String) {
        val fileName = (if (managerName.endsWith("Manager")) managerName else "${managerName}Manager").capitalize()

        val namespace = Managers.buildNamespace(psiDirectory)

        val templateContent = Templates.getTemplateFile("/templates/managers/files/manager.php.template")
            .replace("\${className}", fileName)
            .replace("\${namespace}", namespace)
            .replace("\${managerName}", managerName)

        Files.createFile(
            directory = psiDirectory,
            fileName = "${fileName}.php",
            content = templateContent,
        )
    }
}