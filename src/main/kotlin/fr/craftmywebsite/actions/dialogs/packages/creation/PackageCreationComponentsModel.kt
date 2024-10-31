package fr.craftmywebsite.actions.dialogs.packages.creation

data class PackageCreationComponentsModel(
    val controllers: Boolean,
    val models: Boolean,
    val entities: Boolean,
    val views: Boolean,
    val public: Boolean,
    val events: Boolean,
    val interfaces: Boolean,
    val exceptions: Boolean,
    val type: Boolean,
)