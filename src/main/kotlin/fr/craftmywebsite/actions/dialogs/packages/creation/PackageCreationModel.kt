package fr.craftmywebsite.actions.dialogs.packages.creation

data class PackageCreationModel(
    val name: String,
    val author: String,
    val version: String,
    val components: PackageCreationComponentsModel,
)