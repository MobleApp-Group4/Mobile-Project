package com.example.todo.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.todo.ui.appbars.DetailBottomBar
import com.example.todo.viewmodel.RecipesViewModel
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import org.jsoup.Jsoup


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    navController: NavController,
    recipeViewModel: RecipesViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid
    val isFavorite = remember { mutableStateOf(false) }
    val context = LocalContext.current // 获取当前 Context

    LaunchedEffect(userId,recipeId) {
        if (userId != null){
            userViewModel.isFavorite(userId,recipeId.toString()) { isFav ->
                isFavorite.value = isFav  // 更新 UI
            }
        }
        recipeViewModel.fetchRecipeDetail(recipeId)
    }

    val recipeDetail = recipeViewModel.recipeDetail.value


    if (recipeDetail != null){
        //var isFavorite by remember { mutableStateOf(false) }
        val cleanSummary = Jsoup.parse(recipeDetail.summary).text()
        var isExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = modifier
                .fillMaxSize() // 使用Box填充整个屏幕
        ){
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // 显示食谱图片
                Image(
                    painter = rememberAsyncImagePainter(recipeDetail.image),
                    contentDescription = recipeDetail.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                )

                recipeDetail.diets?.let { diets ->

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        diets.forEach { diet ->
                            AssistChip(
                                onClick = {  },
                                label = { Text(text = diet) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = getDietColor(diet) // 动态生成颜色
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = recipeDetail.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )

                    // 收藏按钮
                    IconButton(onClick = {
                        Log.d("AddFavorite", "Favorite button clicked")  // 确保点击事件被触发
                        isFavorite.value = !isFavorite.value
                        if (isFavorite.value) {
                            if (userId != null){
                                userViewModel.addFavorite(userId,recipeId.toString())  // 添加收藏
                                Log.d("AddFavorite", "Add")  // 确保点击事件被触发
                            }else{
                                Toast.makeText(
                                    context,
                                    "Login Please",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true } // 清空登录栈
                                }
                            }
                        } else {
                            if (userId != null){
                                userViewModel.removeFavorite(userId,recipeId.toString())  // 取消收藏
                            }else{
                                Toast.makeText(
                                    context,
                                    "Login Please",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true } // 清空登录栈
                                }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavorite.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite.value) Color.Red else Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Health Score: ${recipeDetail.healthScore}/100.0")
                Text(text = "Ready in: ${recipeDetail.readyInMinutes} minutes")
//            Text(text = "Price per serving:${recipeDetail.pricePerServing/recipeDetail.servings} EUR")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = cleanSummary,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 6, // 如果展开则显示完整内容
                    overflow = TextOverflow.Ellipsis // 省略超出的部分
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { isExpanded = !isExpanded }) {
                        Text(text = if (isExpanded) "Less <<" else "More >>")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 显示配料列表
                Text(text = "Ingredients:", fontWeight = FontWeight.Bold)
                (recipeDetail.extendedIngredients ?: emptyList()).forEach { ingredient ->
                    Text(text = ingredient.name)
                }

            }
            if (userId != null) {
                DetailBottomBar(
                    navController = navController,
                    userId = userId,
                    recipeId = recipeId,
                    title = recipeDetail?.title ?: "",
                    image = recipeDetail?.image ?: "",
                    userViewModel = userViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
            }
        }


    }

}

@Composable
fun getDietColor(diet: String): Color {
    val hue = (diet.hashCode() % 360).toFloat().let { if (it < 0) it + 360 else it } // 确保 hue 在 0~360
    return Color.hsl(hue, 0.4f, 0.65f)
}


