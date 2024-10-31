package fr.craftmywebsite.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findDirectory
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
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

    fun findPackageRootDirectory(project: Project): PsiDirectory? {
        val virtualFile = project.guessProjectDir()?.findDirectory("App/Package")
            ?: return null

        return PsiManager.getInstance(project).findDirectory(virtualFile)
    }

    fun findPackageDirectory(name: String, project: Project): PsiDirectory? {
        val virtualFile = project.guessProjectDir()?.findDirectory("App/Package/$name")
            ?: return null

        return PsiManager.getInstance(project).findDirectory(virtualFile)
    }

    fun findPackageDirectory(virtualFile: VirtualFile, project: Project): PsiDirectory? {
        var currentDirectory = virtualFile.parent

        while (currentDirectory.name != "Interfaces" && currentDirectory.parent != null) {
            currentDirectory = currentDirectory.parent ?: break
        }

        return PsiManager.getInstance(project).findDirectory(currentDirectory)
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