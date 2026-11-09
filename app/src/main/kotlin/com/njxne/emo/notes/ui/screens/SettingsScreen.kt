package com.njxne.emo.notes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.vibrancy
import com.njxne.emo.notes.data.NoteStore
import com.njxne.emo.notes.ui.theme.AccentDark
import com.njxne.emo.notes.ui.theme.AccentLight
import com.njxne.emo.notes.ui.theme.ThemeMode
import kotlinx.coroutines.flow.collectAsState

@Composable
fun SettingsScreen(
    backdrop: Backdrop,
    isDark: Boolean,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onClearAll: () -> Unit
) {
    val notes by NoteStore.notes.collectAsState()
    val isLight = !isDark
    val titleColor = if (isLight) Color(0xFF1A1C20) else Color.White
    val subColor = if (isLight) Color(0xFF5A5F6A) else Color(0xFFAAB0BE)
    val accent = if (isLight) AccentLight else AccentDark

    var confirmClear by rememberSaveable { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        BasicText(
            "设置",
            Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 32.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = titleColor
            )
        )

        BasicText(
            "主题",
            Modifier.padding(horizontal = 20.dp, top = 16.dp),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                color = subColor
            )
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ThemeOption(
                label = "跟随系统",
                selected = themeMode == ThemeMode.SYSTEM,
                accent = accent,
                contentColor = titleColor,
                onClick = { onThemeModeChange(ThemeMode.SYSTEM) }
            )
            ThemeOption(
                label = "浅色",
                selected = themeMode == ThemeMode.LIGHT,
                accent = accent,
                contentColor = titleColor,
                onClick = { onThemeModeChange(ThemeMode.LIGHT) }
            )
            ThemeOption(
                label = "深色",
                selected = themeMode == ThemeMode.DARK,
                accent = accent,
                contentColor = titleColor,
                onClick = { onThemeModeChange(ThemeMode.DARK) }
            )
        }

        BasicText(
            "数据",
            Modifier.padding(horizontal = 20.dp, top = 24.dp),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                color = subColor
            )
        )
        GlassRow(backdrop, isLight) {
            BasicText(
                "共 ${notes.size} 条笔记",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp,
                    color = titleColor
                )
            )
        }
        GlassRow(backdrop, isLight, onClick = {
            confirmClear = true
        }) {
            BasicText(
                if (confirmClear) "确认清空全部笔记？" else "清空全部笔记",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp,
                    color = if (confirmClear) Color(0xFFE5484D) else titleColor
                )
            )
        }
        if (confirmClear) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ThemeOption(
                    label = "取消",
                    selected = false,
                    accent = accent,
                    contentColor = titleColor,
                    onClick = { confirmClear = false }
                )
                ThemeOption(
                    label = "确认清空",
                    selected = true,
                    accent = Color(0xFFE5484D),
                    contentColor = Color.White,
                    onClick = {
                        onClearAll()
                        confirmClear = false
                    }
                )
            }
        }
        BasicText(
            "",
            Modifier.padding(bottom = 24.dp)
        )
    }
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    accent: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    val surface = if (selected) accent else Color.Black.copy(alpha = 0.06f)
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .padding(vertical = 4.dp)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = label,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.SemiBold else androidx.compose.ui.text.font.FontWeight.Normal,
                color = if (selected) Color.White else contentColor
            )
        )
    }
}

@Composable
private fun GlassRow(
    backdrop: Backdrop,
    isLight: Boolean,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val surface = if (isLight) Color.White.copy(0.35f) else Color.Black.copy(0.45f)
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(20.dp) },
                effects = {
                    vibrancy()
                    blur(10f.dp.toPx())
                },
                onDrawSurface = { drawRect(surface) }
            )
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (onClick != null) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.CenterStart
            ) {
                content()
            }
        } else {
            content()
        }
    }
}
