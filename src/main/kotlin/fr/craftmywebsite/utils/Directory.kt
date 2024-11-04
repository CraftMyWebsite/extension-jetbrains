package fr.craftmywebsite.utils

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager

class Directory {
    companion object {
        fun createDirectory(directory: PsiDirectory, directoryName: String): PsiDirectory? {
            var dir: PsiDirectory? = null

            WriteCommandAction.runWriteCommandAction(directory.project) {
                dir = directory.createSubdirectory(directoryName)
            }

            return dir
        }

        fun getDirectory(file: VirtualFile, project: Project): PsiDirectory? {
            return PsiManager.getInstance(project).findDirectory(file.parent)
        }
    }
}