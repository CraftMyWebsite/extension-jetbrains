package fr.craftmywebsite.utils

object Templates {

    fun getTemplateFile(templatePath: String): String {
        return this::class.java.getResourceAsStream(templatePath)?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalArgumentException("Template file not found at $templatePath")
    }
}