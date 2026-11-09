package com.example.alzapp.data.todo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY date ASC")
    fun getAllTasks(): Flow<List<TodoTaskEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTaskById(id: Int): TodoTaskEntity?

    @Query("SELECT * FROM todos WHERE date LIKE :date || '%'")
    suspend fun getTasksByDate(date: String): List<TodoTaskEntity>
    @Query("SELECT * FROM todos")
    suspend fun getAllTasksSync(): List<TodoTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(todo: TodoTaskEntity): Long

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTaskById(id: Int)
}