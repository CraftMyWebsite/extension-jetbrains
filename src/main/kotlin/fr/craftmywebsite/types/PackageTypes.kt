package fr.craftmywebsite.types

import java.util.*

enum class PackageTypes(val path: String) {
    CONTROLLER("Controllers"),
    MODEL("Models"),
    VIEW("Views"),
    ENTITY("Entities"),
    TYPE("Type"),
    INTERFACE("Interfaces"),
    EVENT("Events");

    val namespace = name.lowercase()
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
}
