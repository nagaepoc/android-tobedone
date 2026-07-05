package com.tobedone.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tobedone.app.data.TodoDatabase
import com.tobedone.app.data.TodoRepository
import com.tobedone.app.data.TodoTask
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository

    /** Exposed as a [StateFlow] so the UI can observe it reactively. */
    val allTasks: StateFlow<List<TodoTask>>

    init {
        val database = TodoDatabase.getDatabase(application)
        repository = TodoRepository(database.todoDao())
        allTasks = repository.allTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            repository.insert(TodoTask(title = title))
        }
    }

    fun toggleTask(task: TodoTask) {
        viewModelScope.launch {
            repository.update(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: TodoTask) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
}
