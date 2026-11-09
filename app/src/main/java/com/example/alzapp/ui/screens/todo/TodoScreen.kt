package com.example.alzapp.ui.screens.todo

import com.example.alzapp.ui.components.CalendarSlider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alzapp.R
import com.example.alzapp.data.todo.local.TodoTaskEntity
import com.example.alzapp.ui.screens.todo.viewModel.CATEGORY_ALL_DAY
import com.example.alzapp.ui.screens.todo.viewModel.CATEGORY_TIMED
import com.example.alzapp.ui.screens.todo.viewModel.TodoViewModel
import com.example.alzapp.utils.AppConfig
import java.time.LocalDate

// stateful component - handles logic and ViewModel, state
@Composable
internal fun TodoScreen(
    viewModel: TodoViewModel,
    onAddTaskClick: (LocalDate) -> Unit = {},
    onTaskClick: (TodoTaskEntity) -> Unit = {},
    highlightedTodoId: Int? = null
) {
    val groupedTodos by viewModel.groupedTodos.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val highlightedId by viewModel.highlightedTodoId.collectAsState()
    val listState = rememberLazyListState()

    // handle highlight if user came from notification
    LaunchedEffect(highlightedTodoId) {
        if (highlightedTodoId != null) {
            viewModel.setHighlightedTodo(highlightedTodoId)
            viewModel.onDateSelected(LocalDate.now())
        }
    }

    TodoContent(
        groupedTodos = groupedTodos,
        selectedDate = selectedDate,
        highlightedId = highlightedId,
        listState = listState,
        onDateSelected = {
            viewModel.onDateSelected(it)
            viewModel.setHighlightedTodo(null)
        },
        onAddTaskClick = { onAddTaskClick(selectedDate) },
        onTaskClick = { task ->
            viewModel.setHighlightedTodo(null)
            onTaskClick(task)
        },
        onToggleTask = { clickedTask ->
            viewModel.updateTodo(clickedTask.copy(isCompleted = !clickedTask.isCompleted))
        }
    )
}

// stateless component - easy to display data in Preview, pure UI
@Composable
fun TodoContent(
    groupedTodos: Map<String, List<TodoTaskEntity>>,
    selectedDate: LocalDate,
    highlightedId: Int?,
    listState: LazyListState = rememberLazyListState(),
    onDateSelected: (LocalDate) -> Unit,
    onAddTaskClick: () -> Unit = {},
    onTaskClick: (TodoTaskEntity) -> Unit = {},
    onToggleTask: (TodoTaskEntity) -> Unit
) {
    // collect all elements (headers and tasks) into a flat list to find indices for scrolling
    val flatList = remember(groupedTodos) {
        val list = mutableListOf<Any>()
        listOf(CATEGORY_TIMED, CATEGORY_ALL_DAY).forEach { category ->
            groupedTodos[category]?.let { tasks ->
                list.add(category) // header
                tasks.forEach { list.add(it) } // task
            }
        }
        list
    }

    // automatically scroll to highlighted task
    LaunchedEffect(highlightedId, flatList) {
        if (highlightedId != null) {
            val indexInFlatList = flatList.indexOfFirst { item ->
                item is TodoTaskEntity && item.id == highlightedId
            }
            
            if (indexInFlatList != -1) {
                // adjust index: +4 because of the 4 items added before the task list (Calendar, TodayButton, Text, Button)
                listState.animateScrollToItem(indexInFlatList + 4)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // calendar; date selector slider
            item {
                CalendarSlider(
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected,
                    locale = AppConfig.PL_LOCALE
                )
            }
            // btn to quickly go back to today's date
            item {
                if (selectedDate != LocalDate.now()) {
                    Button(
                        onClick = { onDateSelected(LocalDate.now()) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text(text = "WRÓĆ DO DZISIAJ", color = MaterialTheme.colorScheme.onTertiary, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            // screen title and current selected date
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "LISTA ZADAŃ",
                    fontWeight = FontWeight.W900,
                    style = MaterialTheme.typography.displaySmall,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = selectedDate.format(AppConfig.DATE_FORMATTER).replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
            // btn to add new task (opens task form)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onAddTaskClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        shape = RoundedCornerShape(32.dp),
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                        modifier = Modifier.heightIn(min = 64.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_add), 
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "DODAJ ZADANIE",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // display appropriate message if there are no tasks for the selected date
            if (groupedTodos.isEmpty()) {
                item {
                    Text(
                        text = "Brak zadań na ten dzień",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // display tasks grouped by those which are timed and those which are not
                listOf(CATEGORY_TIMED, CATEGORY_ALL_DAY).forEach { category ->
                    groupedTodos[category]?.let { tasks ->
                        // custom category header
                        item(key = "header_$category") {
                            TodoCategoryHeader(category)
                        }
                        // list of tasks
                        itemsIndexed(tasks, key = { _, task -> task.id }) { _, task ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                TodoItem(
                                    task = task,
                                    isHighlighted = task.id == highlightedId && !task.isCompleted,
                                    onCompleteClick = { onToggleTask(task) },
                                    onTaskClick = { onTaskClick(task) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}