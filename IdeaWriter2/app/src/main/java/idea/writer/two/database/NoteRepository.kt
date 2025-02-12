package idea.writer.two.database

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()



    suspend fun insert(note: Note): Long = withContext(Dispatchers.IO) {
        noteDao.insertNote(note)
    }
    suspend fun update(note: Note) = withContext(Dispatchers.IO) {
        noteDao.updateNote(note)
    }
    suspend fun delete(note: Note) = withContext(Dispatchers.IO) {
        noteDao.deleteNote(note)
    }

    suspend fun getNote(id: Int): Note? {
        return withContext(Dispatchers.IO) {
            Log.d("NoteRepository", "Fetching note with ID: $id")

            val note = noteDao.getNote(id)

            if (note != null) {
                Log.d("NoteRepository", "Note retrieved: ${note.title}")
            } else {
                Log.d("NoteRepository", "No note found for ID: $id")
            }
            note

        }
    }
}
