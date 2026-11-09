package com.njxne.emo.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.njxne.emo.notes.data.NoteStore

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NoteStore.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            NotesApp()
        }
    }
}
