package fr.craftmywebsite.types

import java.util.*

enum class PackageTypes(val path: String) {
    CONTROLLER("Controllers"),
    MODEL("Models"),
    ENTITY("Entities"),
    TYPE("Type"),
    INTERFACE("Interfaces"),
    IMPLEMENTATION("Implementations"),
    EVENT("Events"),
    EXCEPTION("Exceptions"),

    //No namespace needed
    VIEW("Views"),
    PUBLIC("Public"),
    INIT("Init"),
    LANG("Lang");

    val namespace = name.lowercase()
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
}
