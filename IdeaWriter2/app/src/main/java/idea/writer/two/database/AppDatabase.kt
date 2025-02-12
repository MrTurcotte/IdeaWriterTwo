package idea.writer.two.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val executorService = Executors.newCachedThreadPool()
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_db"
                ).setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .setQueryExecutor(executorService)  // Custom Executor
                    .setTransactionExecutor(executorService)  // Custom Executor for transactions
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
