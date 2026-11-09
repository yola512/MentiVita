package com.example.alzapp.data.todo

import com.example.alzapp.data.todo.local.TodoDao
import com.example.alzapp.data.todo.local.TodoTaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val dao: TodoDao
){
    val allTodoTasks: Flow<List<TodoTaskEntity>> = dao.getAllTasks()
    suspend fun getTaskById(id: Int): TodoTaskEntity? {
        return dao.getTaskById(id)
    }

    suspend fun getTasksByDate(date: String): List<TodoTaskEntity> {
        return dao.getTasksByDate(date)
    }

    suspend fun getAllTasksSync(): List<TodoTaskEntity> {
        return dao.getAllTasksSync()
    }

    suspend fun addTodoTask(todo: TodoTaskEntity): Long {
        return dao.insertTask(todo)
    }
    suspend fun deleteTodoTask(id: Int) {
        dao.deleteTaskById(id)
    }
    suspend fun updateTodoTask(todo: TodoTaskEntity) {
        dao.insertTask(todo)
    }
}