package fr.craftmywebsite.utils

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import fr.craftmywebsite.types.PackageTypes

class Files {

    companion object {
        fun createFile(directory: PsiDirectory, fileName: String, content: String) {
            WriteCommandAction.runWriteCommandAction(directory.project) {
                val newFile = directory.createFile(fileName)
                newFile.viewProvider.document?.setText(content)
            }
        }

        fun isInAllowedPackageFolder(virtualFile: VirtualFile?, type: PackageTypes): Boolean {
            if (virtualFile == null || !virtualFile.isDirectory) return false

            var currentDirectory = virtualFile

            while (currentDirectory != null) {
                if (currentDirectory.name == type.path) return true
                currentDirectory = currentDirectory.parent
            }

            return false
        }
    }

}