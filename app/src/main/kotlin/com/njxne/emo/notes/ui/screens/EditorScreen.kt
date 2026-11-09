package com.njxne.emo.notes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.vibrancy
import com.njxne.emo.notes.ui.theme.Icons

@Composable
fun EditorScreen(
    noteId: String?,
    initialTitle: String,
    initialContent: String,
    backdrop: Backdrop,
    isDark: Boolean,
    onBack: () -> Unit,
    onSave: (title: String, content: String) -> Unit,
    onDelete: () -> Unit
) {
    val isLight = !isDark
    val titleColor = if (isLight) Color(0xFF1A1C20) else Color.White
    val subColor = if (isLight) Color(0xFF5A5F6A) else Color(0xFFAAB0BE)
    val accent = if (isLight) Color(0xFF0088FF) else Color(0xFF3FA9FF)
    val surface = if (isLight) Color.White.copy(0.35f) else Color.Black.copy(0.45f)

    var title by remember(noteId) { mutableStateOf(initialTitle) }
    var content by remember(noteId) { mutableStateOf(initialContent) }

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    Icons.ArrowBack,
                    null,
                    Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(titleColor)
                )
            }
            BasicText(
                if (noteId == null) "新建笔记" else "编辑笔记",
                Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                ),
                textAlign = TextAlign.Center
            )
            Box(
                Modifier
                    .size(40.dp)
                    .clickable(onClick = {
                        if (title.isBlank() && content.isBlank()) {
                            onBack()
                        } else {
                            onSave(title, content)
                        }
                    }),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    "保存",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = accent
                    )
                )
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, top = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { RoundedCornerShape(16.dp) },
                    effects = {
                        vibrancy()
                        blur(10f.dp.toPx())
                    },
                    onDrawSurface = { drawRect(surface) }
                )
                .padding(16.dp)
        ) {
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, top = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { RoundedCornerShape(16.dp) },
                    effects = {
                        vibrancy()
                        blur(10f.dp.toPx())
                    },
                    onDrawSurface = { drawRect(surface) }
                )
                .padding(16.dp)
        ) {
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = titleColor
                ),
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, bottom = 24.dp)
                .padding(bottom = 16.dp)
        ) {
            if (noteId != null) {
                BasicText(
                    "删除此笔记",
                    Modifier
                        .clickable(onClick = onDelete)
                        .padding(vertical = 8.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFFE5484D)
                    )
                )
            }
        }
    }
}
