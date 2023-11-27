data class Data(val name: String, val theme: String? = null)

class Index(size: Int) {
    private val digits = (2 * size).toString().length
    private var value = 1
    val next: String get() = value++.toString().padStart(digits, '0')
}

val list = listOf(
    Data("Material Theme"),
    Data("Red Wallpaper", "RED_DOMINATED_EXAMPLE"),
    Data("Green Wallpaper", "GREEN_DOMINATED_EXAMPLE"),
    Data("Blue Wallpaper", "BLUE_DOMINATED_EXAMPLE"),
    Data("Yellow Wallpaper", "YELLOW_DOMINATED_EXAMPLE"),
).apply {
    val index = Index(size)
    forEach {
        println(makeTexts(it, index.next, true))
        println(makeTexts(it, index.next, false))
    }
    println("annotation class ThemesPreview")
}

fun makeTexts(item: Data, index: String, isDay: Boolean) = buildString {
    append("@Preview(\n")
    append("    name = \"$index - ${item.name} - ${if (isDay) "Day" else "Night"}\",\n")
    append("    showBackground = true,\n")
    append("    heightDp = SHORT_HEIGHT,\n")
    item.theme?.let { append("    wallpaper = Wallpapers.$it,\n") }
    if (!isDay) append("    uiMode = Configuration.UI_MODE_NIGHT_YES,\n")
    append(")")
}
