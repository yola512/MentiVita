package com.example.alzapp.ui.screens.todo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alzapp.data.todo.TodoRepository
import com.example.alzapp.data.todo.TodoScheduler
import com.example.alzapp.data.todo.local.TodoTaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

const val CATEGORY_TIMED = "Ważne wydarzenia"
const val CATEGORY_ALL_DAY = "Moje sprawy"

@HiltViewModel
internal class TodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val scheduler: TodoScheduler // notifications
) : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _highlightedTodoId = MutableStateFlow<Int?>(null)
    val highlightedTodoId: StateFlow<Int?> = _highlightedTodoId

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setHighlightedTodo(id: Int?) {
        _highlightedTodoId.value = id
    }

    val todos: StateFlow<List<TodoTaskEntity>> = repository.allTodoTasks
        .combine(_selectedDate) { tasks, date ->
            tasks.filter { it.date.toLocalDate() == date } // tasks for specific date
                .sortedWith(compareBy<TodoTaskEntity> { it.time == null }
                .thenBy { it.time }
                .thenByDescending { it.id })
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // groups todos into categories
    val groupedTodos: StateFlow<Map<String, List<TodoTaskEntity>>> = todos
        .map { tasks ->
            tasks.groupBy { task ->
                if (task.time != null) CATEGORY_TIMED else CATEGORY_ALL_DAY
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // essential for task form
    suspend fun getTaskById(id: Int): TodoTaskEntity? {
        return repository.getTaskById(id)
    }

    // adds new task and sets reminder if needed
    fun addTodo(todo: TodoTaskEntity) = viewModelScope.launch {
        val id = repository.addTodoTask(todo)
        val savedTask = todo.copy(id = id.toInt())
        if (savedTask.time != null && savedTask.reminderMinutesBefore != null) {
            scheduler.scheduleTaskReminder(savedTask)
        }
    }

    // deletes task and cancels reminder
    fun deleteTodo(todo: TodoTaskEntity) = viewModelScope.launch {
        scheduler.cancelTaskReminder(todo)
        repository.deleteTodoTask(todo.id)
    }

    // updates task and decides whether to cancel reminder or not
    fun updateTodo(todo: TodoTaskEntity) = viewModelScope.launch {
        if (todo.isCompleted || todo.time == null || todo.reminderMinutesBefore == null) {
            scheduler.cancelTaskReminder(todo)
        } else {
            scheduler.scheduleTaskReminder(todo)
        }
        repository.updateTodoTask(todo)
    }
}
