package fr.craftmywebsite.actions.dialogs.themes.creation

data class ThemeCreationModel(
    val name: String,
    val author: String,
    val version: String,
    val compatiblePackages: List<String>,
    val requiredPackages: List<String>
)