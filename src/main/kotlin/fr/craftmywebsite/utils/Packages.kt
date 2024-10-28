package fr.craftmywebsite.utils

import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes

object Packages {
    fun findPackageName(directory: PsiDirectory): String {
        var currentDirectory = directory
        while (currentDirectory.parent != null) {
            if (currentDirectory.parent?.name == "Package") {
                return currentDirectory.name
            }
            currentDirectory = currentDirectory.parent ?: break
        }
        throw IllegalArgumentException("Unable to find 'Package' folder.")
    }

    fun buildNamespace(directory: PsiDirectory, type: PackageTypes): String {
        val packagePath = mutableListOf<String>()
        var currentDirectory = directory
        while (currentDirectory.name != "Package" && currentDirectory.parent != null) {
            if (currentDirectory.name != type.path) {
                packagePath.add(currentDirectory.name)
            }
            currentDirectory = currentDirectory.parent ?: break
        }
        if (currentDirectory.name != "Package") throw IllegalArgumentException("Unable to find 'Package' folder.")
        return "CMW\\${type.namespace}\\" + packagePath.asReversed().joinToString("\\")
    }
}