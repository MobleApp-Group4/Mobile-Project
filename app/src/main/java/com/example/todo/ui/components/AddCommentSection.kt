package com.example.todo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.viewmodel.RecipesViewModel

@Composable
fun AddCommentSection(
    recipeId: String,
    userId: String,
    recipesViewModel: RecipesViewModel
) {
    var commentText by remember { mutableStateOf("") }
    var selectedRating by remember { mutableIntStateOf(0) }

    Column {
        Text(text = "Your comment", fontSize = 20.sp, modifier = Modifier)
        Spacer(modifier = Modifier.height(8.dp))

        RatingBar(
            selectedRating = selectedRating,
            onRatingSelected = { newRating -> selectedRating = newRating } // Update selectedRating
        )

        Text(text = "Your Rating: $selectedRating", fontSize = 16.sp)



        Spacer(modifier = Modifier.height(16.dp))

        // Comment input box
        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text("Write a comment") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        // The button of "submit"
        Button(
            onClick = {
                recipesViewModel.addComment(
                    recipeId = recipeId,
                    userId = userId,
                    rating = selectedRating.toDouble(),
                    text = commentText
                )
                commentText = ""  // Empty the input box
                selectedRating = 0  // Reset the score
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Submit Comment")
        }
    }
}