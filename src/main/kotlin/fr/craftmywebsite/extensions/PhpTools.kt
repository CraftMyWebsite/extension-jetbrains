package fr.craftmywebsite.extensions

fun List<String>.toPhpArray(): String {
    return if (this.isEmpty()) "[]" else this.joinToString("\", \"", "[\"", "\"]")
}
