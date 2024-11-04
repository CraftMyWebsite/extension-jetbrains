package fr.craftmywebsite.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.findDirectory
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager

object Themes {
    fun findThemeRootDirectory(project: Project): PsiDirectory? {
        val virtualFile = project.guessProjectDir()?.findDirectory("Public/Themes")
            ?: return null

        return PsiManager.getInstance(project).findDirectory(virtualFile)
    }

    fun isThemeNameExist(themeName: String, project: Project): Boolean {
        val rootThemeDirectory = findThemeRootDirectory(project) ?: return false
        return rootThemeDirectory.findSubdirectory(themeName) != null
    }
}