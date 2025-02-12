package idea.writer.two.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepository(noteDao)
    }
}


//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    @Singleton
//    @Provides
//    fun provideNoteRepository(
//        @ApplicationContext context: Context
//    ): NoteRepository {
//        return Room.databaseBuilder(
//            context,
//            NoteRepository::class.java,
//            "note_database"
//        ).build()
//    }
//
//    @Provides
//    fun provideNoteDao(database: AppDatabase): NoteDao {
//        return database.noteDao()
//    }

//    @Singleton
//    @Provides
//    fun provideNoteDao(noteDatabase: AppDatabase): NoteDao {
//        return noteDatabase.noteDao()
//    }
//
//    @Singleton
//    @Provides
//    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
//        return NoteRepository(noteDao)
//    }
