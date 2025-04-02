package com.example.todo.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(selectedRating: Int, onRatingSelected: (Int) -> Unit) {
    Row(modifier = Modifier) {
        (1..5).forEach { rating ->
            IconButton(onClick = { onRatingSelected(rating) }) { // 点击时调用回调函数
                Icon(
                    imageVector = if (rating <= selectedRating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Rating $rating",
                    tint =  if (rating <= selectedRating) Color.Yellow else Color.Gray
                )
            }
        }
    }
}
