package com.example.todo.components

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.todo.model.Recipe

@Composable
fun RecipeList(recipes: List<Recipe>,navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(recipes) { recipe ->
            RecipeItem(recipe,navController)
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe,navController: NavController) {

    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 电影封面
            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 电影标题
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                modifier = Modifier
                    .clickable {
                        navController.navigate("recipes/${recipe.id}")
                    }
            )
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // 让评分在左，收藏按钮在右
            ) {
                // 星星图标和评分文本
                Row(
                    verticalAlignment = Alignment.CenterVertically // 垂直居中
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = Color(0xFFFFD700), // 金黄色
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "vote", // 这里可以替换为具体评分数据
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray,
                            fontSize = 20.sp // 设置较小的字体大小
                        )
                    )
                }

                // 收藏按钮
//                IconButton(onClick = { /* 添加收藏逻辑 */ }) {
//                    Icon(
//                        imageVector = Icons.Default.FavoriteBorder,
//                        contentDescription = "Favorite Icon",
//                        tint = Color.Gray // 收藏图标颜色
//                    )
//                }
            }
        }
    }
}