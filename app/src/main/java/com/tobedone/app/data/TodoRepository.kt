package com.tobedone.app.data

import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for task data. Hides the DAO implementation
 * from the rest of the app so the data layer can evolve independently.
 */
class TodoRepository(private val todoDao: TodoDao) {

    val allTasks: Flow<List<TodoTask>> = todoDao.getAllTasks()

    suspend fun insert(task: TodoTask) {
        todoDao.insertTask(task)
    }

    suspend fun update(task: TodoTask) {
        todoDao.updateTask(task)
    }

    suspend fun delete(task: TodoTask) {
        todoDao.deleteTask(task)
    }
}
