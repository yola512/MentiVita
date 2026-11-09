package com.example.alzapp.ui.screens.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.alzapp.ui.screens.recipes.viewModel.RecipeViewModel
import com.example.alzapp.ui.components.CustomBackButton
import com.example.alzapp.ui.components.SectionHeader

@Composable
internal fun RecipeDetails(
    recipeId: Int,
    viewModel: RecipeViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val recipe by viewModel.getRecipeById(recipeId).collectAsState(initial = null)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // back button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    CustomBackButton(verticalPadding = 8.dp, iconSize = 60.dp, onClick = onBackClick, color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            if (recipe == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Ładowanie przepisu...", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                val recipe = recipe!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            shape = RoundedCornerShape(24.dp),
                            shadowElevation = 7.dp
                        ) {
                            AsyncImage(
                                model = recipe.imageRes,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.2f)
                            )
                        }
                    }

                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 32.dp),
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor =  MaterialTheme.colorScheme.onPrimary),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = recipe.name,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )

                                // recipe info row (time and difficulty) using FlowRow to handle wrapping
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    RecipeInfoItem(
                                        icon = Icons.Default.Schedule,
                                        text = recipe.cookingTime.ifEmpty { "N/A" }
                                    )
                                    RecipeInfoItem(
                                        icon = Icons.Default.SignalCellularAlt,
                                        text = recipe.difficulty.ifEmpty { "N/A" }
                                    )
                                }

                                // tools section with bullet points
                                if (recipe.tools.isNotEmpty()) {
                                    SectionHeader(title = "Przybory", verticalPadding = 10.dp)
                                    Column(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        recipe.tools.forEach { tool ->
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(10.dp)
                                                        .background(MaterialTheme.colorScheme.onSurface, CircleShape)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = tool,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.W600,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    lineHeight = 35.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                // ingredients
                                SectionHeader(title = "Składniki", verticalPadding = 10.dp)

                                Column(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                                ) {
                                    recipe.ingredients.forEach { ingredient ->
                                        val parts = ingredient.split(" - ")
                                        if (parts.size == 2) {
                                            IngredientRow(name = parts[0], amount = parts[1])
                                        } else {
                                            Text(
                                                text = "• $ingredient",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                lineHeight = 35.sp
                                            )
                                        }
                                    }
                                }

                                // recipe steps
                                SectionHeader(title = "Sposób przygotowania", verticalPadding = 10.dp)
                                
                                recipe.steps.forEachIndexed { index, step ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = "${index + 1}.",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.width(60.dp)
                                        )
                                        Text(
                                            text = step,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.W600,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 35.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
