package com.example.room_persistence.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note:Note)

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes():List<Note>

    @Query("SELECT * FROM notes WHERE id= :id")
    suspend fun getNoteById(id:Int):Note?

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id:Int)
}