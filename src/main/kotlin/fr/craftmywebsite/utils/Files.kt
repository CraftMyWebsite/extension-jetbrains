package fr.craftmywebsite.utils

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDirectory

class Files {

    companion object {
        fun createFile(directory: PsiDirectory, fileName: String, content: String) {
            WriteCommandAction.runWriteCommandAction(directory.project) {
                val newFile = directory.createFile(fileName)
                newFile.viewProvider.document?.setText(content)
            }
        }
    }

}