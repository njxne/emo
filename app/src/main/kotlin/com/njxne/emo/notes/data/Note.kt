package com.njxne.emo.notes.data

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val favorite: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
