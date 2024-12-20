package com.example.room_persistence.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.room_persistence.R
import com.example.room_persistence.room.Note
import com.example.room_persistence.room.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var buttonSave: Button
    private lateinit var noteDatabase: NoteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)
        buttonSave = findViewById(R.id.buttonSave)

        noteDatabase = NoteDatabase.getDatabase(this)

        buttonSave.setOnClickListener{
            saveNote()
        }
    }

    private fun  saveNote(){
        val title= editTextTitle.text.toString()
        val content = editTextContent.text.toString()
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val note = Note(title = title, content = content, timestamp = timestamp)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                noteDatabase.NoteDao().insert(note)
                runOnUiThread{
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }catch (e: Exception){
                runOnUiThread{
                    Toast.makeText(this@AddNoteActivity, "Failed to Save Note: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (title.isEmpty()|| content.isEmpty()){
            Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}