package com.example.room_persistence.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.room_persistence.R
import com.example.room_persistence.room.MainViewModel
import com.example.room_persistence.room.Note

class EditNoteActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var saveButton: Button

    private var noteId: Int = 0
    private lateinit var originalNote: Note

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val note=intent.getSerializableExtra("note") as? Note
        note?.let {
            originalNote = it
            noteId = it.id
        }

        editTitle = findViewById(R.id.editTitleField)
        editContent = findViewById(R.id.editContentField)
        saveButton = findViewById(R.id.buttonSave)

        editTitle.setText(originalNote.title)
        editContent.setText(originalNote.content)

        saveButton.setOnClickListener{
            val updateTitle = editTitle.text.toString()
            val updateContent= editContent.text.toString()

            if (updateTitle.isNotEmpty() && updateContent.isNotEmpty()){
                val updateNote = Note(
                    id = originalNote.id,
                    title = updateTitle,
                    content = updateContent,
                    timestamp = originalNote.timestamp
                )

                mainViewModel.updateNote(updateNote)

                val resultIntent= Intent().apply {
                    putExtra("updateNote", updateNote)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()

            }else{
                Toast.makeText(this, "Title And Content Cannot Be Empty", Toast.LENGTH_SHORT).show()
            }
        }


    }
}