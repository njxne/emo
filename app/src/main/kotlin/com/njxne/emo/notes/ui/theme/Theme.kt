package com.njxne.emo.notes.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

object ThemePrefs {
    const val FILE = "emo_notes"
    const val KEY_THEME = "theme"
}

val LightWallpaper: Brush = Brush.linearGradient(
    listOf(Color(0xFFEEF1FF), Color(0xFFDCE4FF), Color(0xFFE8D4FF), Color(0xFFFFD9E8))
)

val DarkWallpaper: Brush = Brush.linearGradient(
    listOf(Color(0xFF0E1030), Color(0xFF1B1655), Color(0xFF3E1C6E), Color(0xFF5A1B5E))
)

val AccentLight = Color(0xFF0088FF)
val AccentDark = Color(0xFF3FA9FF)
val StarGold = Color(0xFFFFB300)
