package idea.writer.two.app

import android.app.Application
import idea.writer.two.database.AppDatabase
import idea.writer.two.database.NoteRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IdeaWriterTwo : Application() {
    lateinit var repository: NoteRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getDatabase(this)
        repository = NoteRepository(database.noteDao())
    }
}
