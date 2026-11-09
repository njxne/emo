package com.njxne.emo.notes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.njxne.emo.notes.data.Note
import com.njxne.emo.notes.data.NoteStore
import com.njxne.emo.notes.ui.theme.Icons
import com.njxne.emo.notes.ui.theme.StarGold
import kotlinx.coroutines.flow.collectAsState

@Composable
fun NotesScreen(
    backdrop: Backdrop,
    isDark: Boolean,
    onOpenNote: (Note) -> Unit,
    onNewNote: () -> Unit
) {
    val notes by NoteStore.notes.collectAsState()
    val isLight = !isDark
    val titleColor = if (isLight) Color(0xFF1A1C20) else Color.White

    Column(Modifier.fillMaxSize()) {
        BasicText(
            "笔记",
            Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 32.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = titleColor
            )
        )
        NotesList(
            backdrop = backdrop,
            isLight = isLight,
            notes = notes.sortedByDescending { it.updatedAt },
            onOpenNote = onOpenNote,
            onNewNote = onNewNote
        )
    }
}

@Composable
fun FavoritesScreen(
    backdrop: Backdrop,
    isDark: Boolean,
    onOpenNote: (Note) -> Unit,
    onNewNote: () -> Unit
) {
    val notes by NoteStore.notes.collectAsState()
    val isLight = !isDark
    val titleColor = if (isLight) Color(0xFF1A1C20) else Color.White

    Column(Modifier.fillMaxSize()) {
        BasicText(
            "收藏",
            Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 32.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = titleColor
            )
        )
        NotesList(
            backdrop = backdrop,
            isLight = isLight,
            notes = notes.filter { it.favorite }.sortedByDescending { it.updatedAt },
            onOpenNote = onOpenNote,
            onNewNote = onNewNote
        )
    }
}

@Composable
private fun NotesList(
    backdrop: Backdrop,
    isLight: Boolean,
    notes: List<Note>,
    onOpenNote: (Note) -> Unit,
    onNewNote: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            if (notes.isEmpty()) {
                item {
                    BasicText(
                        "还没有笔记",
                        Modifier
                            .statusBarsPadding()
                            .fillMaxWidth()
                            .padding(top = 60.dp)
                            .padding(horizontal = 20.dp),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            color = if (isLight) Color(0xFF5A5F6A) else Color(0xFFAAB0BE)
                        )
                    )
                }
            }
            items(notes.size) { i ->
                NoteCard(
                    note = notes[i],
                    backdrop = backdrop,
                    isLight = isLight,
                    onClick = { onOpenNote(notes[i]) },
                    onToggleFavorite = {
                        NoteStore.setFavorite(notes[i].id, !notes[i].favorite)
                    }
                )
            }
            if (notes.isNotEmpty()) {
                item {
                    BasicText(
                        "共 ${notes.size} 条",
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .padding(horizontal = 20.dp),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp,
                            color = if (isLight) Color(0xFF8A90A0) else Color(0xFF7A8095)
                        )
                    )
                }
            }
        }

        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .size(56.dp)
                .padding(horizontal = 16.dp, bottom = 20.dp)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { CircleShape },
                    effects = {
                        vibrancy()
                        blur(8f.dp.toPx())
                        lens(20f.dp.toPx(), 20f.dp.toPx())
                    },
                    onDrawSurface = { drawRect(Color.Black.copy(alpha = 0.08f)) }
                )
                .clickable(onClick = onNewNote),
            contentAlignment = Alignment.Center
        ) {
            Image(
                Icons.Add,
                null,
                Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    backdrop: Backdrop,
    isLight: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val titleColor = if (isLight) Color(0xFF1A1C20) else Color.White
    val subColor = if (isLight) Color(0xFF5A5F6A) else Color(0xFFAAB0BE)
    val surface = if (isLight) Color.White.copy(0.35f) else Color.Black.copy(0.45f)
    val radius = 20.dp

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(radius))
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(radius) },
                effects = {
                    vibrancy()
                    blur(10f.dp.toPx())
                },
                onDrawSurface = { drawRect(surface) }
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            BasicText(
                text = note.title.ifBlank { "无标题" },
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    color = titleColor
                ),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            if (note.content.isNotBlank()) {
                BasicText(
                    text = note.content,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 13.sp,
                        color = subColor
                    ),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
            BasicText(
                text = NoteStore.formatTime(note.updatedAt),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 11.sp,
                    color = subColor
                )
            )
        }
        Box(
            Modifier
                .clickable(onClick = onToggleFavorite)
                .padding(start = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                if (note.favorite) Icons.Star else Icons.StarOutline,
                null,
                Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(
                    if (note.favorite) StarGold else subColor
                )
            )
        }
    }
}
