package com.njxne.emo.notes.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object NoteStore {

    private lateinit var file: File
    private val notes = MutableStateFlow<List<Note>>(emptyList())

    val notes: StateFlow<List<Note>> = notes

    fun init(context: Context) {
        if (::file.isInitialized) return
        file = File(context.filesDir, "notes.json")
        load()
    }

    private fun load() {
        if (!file.exists()) return
        runCatching {
            val array = JSONArray(file.readText())
            notes.value = List(array.length()) { i ->
                val o = array.getJSONObject(i)
                Note(
                    id = o.getString("id"),
                    title = o.getString("title"),
                    content = o.getString("content"),
                    favorite = o.optBoolean("favorite", false),
                    createdAt = o.getLong("createdAt"),
                    updatedAt = o.getLong("updatedAt")
                )
            }
        }
    }

    private fun persist() {
        runCatching {
            val array = JSONArray()
            notes.value.forEach { n ->
                array.put(
                    JSONObject().apply {
                        put("id", n.id)
                        put("title", n.title)
                        put("content", n.content)
                        put("favorite", n.favorite)
                        put("createdAt", n.createdAt)
                        put("updatedAt", n.updatedAt)
                    }
                )
            }
            file.writeText(array.toString())
        }
    }

    fun create(title: String, content: String) {
        val now = System.currentTimeMillis()
        val note = Note(UUID.randomUUID().toString(), title.trim(), content, false, now, now)
        notes.value = notes.value + note
        persist()
    }

    fun update(id: String, title: String, content: String) {
        notes.value = notes.value.map {
            if (it.id == id) it.copy(
                title = title.trim(),
                content = content,
                updatedAt = System.currentTimeMillis()
            ) else it
        }
        persist()
    }

    fun setFavorite(id: String, favorite: Boolean) {
        notes.value = notes.value.map {
            if (it.id == id) it.copy(favorite = favorite) else it
        }
        persist()
    }

    fun delete(id: String) {
        notes.value = notes.value.filterNot { it.id == id }
        persist()
    }

    fun clearAll() {
        notes.value = emptyList()
        persist()
    }

    fun formatTime(timestamp: Long): String =
        SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(timestamp))
}
