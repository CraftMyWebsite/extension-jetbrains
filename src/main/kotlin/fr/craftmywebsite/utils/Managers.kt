package fr.craftmywebsite.utils

import com.intellij.psi.PsiDirectory

object Managers {

    fun buildNamespace(directory: PsiDirectory): String {
        val packagePath = mutableListOf<String>()
        var currentDirectory = directory
        while (currentDirectory.name != "Manager" && currentDirectory.parent != null) {
            packagePath.add(currentDirectory.name)

            currentDirectory = currentDirectory.parent ?: break
        }
        if (currentDirectory.name != "Manager") throw IllegalArgumentException("Unable to find 'Manager' folder.")
        return "CMW\\Manager\\" + packagePath.asReversed().joinToString("\\")
    }
}