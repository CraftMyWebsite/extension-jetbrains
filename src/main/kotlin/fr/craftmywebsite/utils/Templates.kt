package fr.craftmywebsite.utils

import com.intellij.psi.PsiDirectory

object Templates {

    fun getTemplateFile(templatePath: String): String {
        return this::class.java.getResourceAsStream(templatePath)?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalArgumentException("Template file not found at $templatePath")
    }

    fun createTemplateFile(templatePath: String, directory: PsiDirectory) {
        val data = this::class.java.getResourceAsStream(templatePath)?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalArgumentException("Template file not found at $templatePath")

        Files.createFile(directory, pathToFileName(templatePath), data)
    }

    fun createTemplateFile(templatePath: String, directory: PsiDirectory, fileName: String) {
        val data = this::class.java.getResourceAsStream(templatePath)?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalArgumentException("Template file not found at $templatePath")

        Files.createFile(directory, fileName, data)
    }

    private fun pathToFileName(fileName: String): String {
        return fileName.substringAfterLast("/").replace(".template", "")
    }

}