package com.njxne.emo.notes.ui.components.liquid

import androidx.compose.runtime.withFrameNanos

suspend fun awaitFrame() {
    withFrameNanos { }
}
