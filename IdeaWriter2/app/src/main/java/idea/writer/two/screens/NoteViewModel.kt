package idea.writer.two.screens

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import idea.writer.two.database.Note
import idea.writer.two.database.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository, val savedStateHandle: SavedStateHandle) : ViewModel() {
    val allNotes = repository.allNotes
    var currentNoteId: Int? = null

    private val _noteTitle = MutableStateFlow("")
    val noteTitle: StateFlow<String> = _noteTitle

    private val _noteContent = MutableStateFlow("")
    val noteContent: StateFlow<String> = _noteContent

    private val _titleTextField = MutableStateFlow(TextFieldValue(""))
            val titleTextField: StateFlow<TextFieldValue> = _titleTextField
    private val _contentTextField = MutableStateFlow(TextFieldValue(""))
    val contentTextField: StateFlow<TextFieldValue> = _contentTextField

    init {
        savedStateHandle.get<Int>("id")?.let { id ->
            if (id != -1) {
                viewModelScope.launch {
                    Log.d("NoteViewModel", "Fetching note with ID: $id")

                    repository.getNote(id)?.also { currentNote ->
                        Log.d("NoteViewModel", "Note found: ${currentNote.title}")

                        currentNoteId = currentNote.id
                        _noteTitle.value = titleTextField.value.text
                        _noteContent.value = contentTextField.value.text
                    } ?: run {
                        Log.d("NoteViewModel", "No note found for ID: $id")
                    }
                }
            }
        }
    }

    fun insert() = viewModelScope.launch {
        val note = Note(
            id = currentNoteId,
            title = titleTextField.value.text,
            content = contentTextField.value.text,
            timestamp = System.currentTimeMillis()
        )
        val newId = repository.insert(note).toInt()
        if (currentNoteId != newId) {
            currentNoteId = newId
        }

    }
    fun delete(note: Note) = viewModelScope.launch { repository.delete(note) }
    fun getNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedNote = repository.getNote(id)
            withContext(Dispatchers.Main) {
                fetchedNote?.let {
                    currentNoteId = it.id
                    _titleTextField.value = TextFieldValue(it.title)
                    _contentTextField.value = TextFieldValue(it.content)
                    Log.d("NoteViewModel", "Fetched Note - Title: ${noteTitle.value}, Content: ${noteContent.value}")
                }
            }
        }
    }
    fun updateTitleTextField(newTitle: TextFieldValue) {
        _titleTextField.value = newTitle
        insert()
    }
    fun updateContentTextField(newContent: TextFieldValue) {
        _contentTextField.value = newContent
        insert()
    }
}
