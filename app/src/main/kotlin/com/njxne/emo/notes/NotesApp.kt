package com.njxne.emo.notes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.weight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.njxne.emo.notes.data.NoteStore
import com.njxne.emo.notes.ui.components.BottomTabItem
import com.njxne.emo.notes.ui.components.LiquidBottomBar
import com.njxne.emo.notes.ui.screens.EditorScreen
import com.njxne.emo.notes.ui.screens.FavoritesScreen
import com.njxne.emo.notes.ui.screens.NotesScreen
import com.njxne.emo.notes.ui.screens.SettingsScreen
import com.njxne.emo.notes.ui.theme.DarkWallpaper
import com.njxne.emo.notes.ui.theme.Icons
import com.njxne.emo.notes.ui.theme.LightWallpaper
import com.njxne.emo.notes.ui.theme.ThemeMode
import com.njxne.emo.notes.ui.theme.ThemePrefs
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

data class EditorUiState(
    val id: String?,
    val title: String,
    val content: String
)

@Composable
fun NotesApp() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences(ThemePrefs.FILE, android.content.Context.MODE_PRIVATE)
    }
    var themeMode by remember {
        mutableStateOf(
            runCatching {
                ThemeMode.valueOf(
                    prefs.getString(ThemePrefs.KEY_THEME, ThemeMode.SYSTEM.name)
                )
            }.getOrDefault(ThemeMode.SYSTEM)
        )
    }
    LaunchedEffect(themeMode) {
        prefs.edit().putString(ThemePrefs.KEY_THEME, themeMode.name).apply()
    }

    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    var editor by remember { mutableStateOf<EditorUiState?>(null) }

    val backdrop = rememberLayerBackdrop()

    val view = LocalView.current
    SideEffect {
        WindowInsetsControllerCompat(view.window, view).isAppearanceLightStatusBars = !isDark
    }

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .paint(if (isDark) DarkWallpaper else LightWallpaper)
                .layerBackdrop(backdrop)
        )

        Column(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                when (tabIndex) {
                    0 -> NotesScreen(
                        backdrop = backdrop,
                        isDark = isDark,
                        onOpenNote = { note ->
                            editor = EditorUiState(note.id, note.title, note.content)
                        },
                        onNewNote = { editor = EditorUiState(null, "", "") }
                    )
                    1 -> FavoritesScreen(
                        backdrop = backdrop,
                        isDark = isDark,
                        onOpenNote = { note ->
                            editor = EditorUiState(note.id, note.title, note.content)
                        },
                        onNewNote = { editor = EditorUiState(null, "", "") }
                    )
                    else -> SettingsScreen(
                        backdrop = backdrop,
                        isDark = isDark,
                        themeMode = themeMode,
                        onThemeModeChange = { themeMode = it },
                        onClearAll = { NoteStore.clearAll() }
                    )
                }
            }
            Box(
                Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 8.dp)
            ) {
                LiquidBottomBar(
                    items = listOf(
                        BottomTabItem(Icons.Notes, "笔记"),
                        BottomTabItem(Icons.Star, "收藏"),
                        BottomTabItem(Icons.Settings, "设置"),
                    ),
                    selectedTabIndex = { tabIndex },
                    onTabSelected = { tabIndex = it },
                    backdrop = backdrop
                )
            }
        }

        editor?.let { state ->
            EditorScreen(
                noteId = state.id,
                initialTitle = state.title,
                initialContent = state.content,
                backdrop = backdrop,
                isDark = isDark,
                onBack = { editor = null },
                onSave = { title, content ->
                    if (state.id == null) {
                        NoteStore.create(title, content)
                    } else {
                        NoteStore.update(state.id, title, content)
                    }
                    editor = null
                },
                onDelete = {
                    if (state.id != null) {
                        NoteStore.delete(state.id)
                    }
                    editor = null
                }
            )
        }
    }
}
