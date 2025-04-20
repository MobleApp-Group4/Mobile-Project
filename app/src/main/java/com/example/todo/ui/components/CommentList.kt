package com.example.todo.ui.components

import android.icu.text.SimpleDateFormat
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.model.Comment
import java.util.Locale


@Composable
fun CommentList(comments: List<Comment>) {
    Log.d("CommentList", "comments = $comments")  // 🔍 打印所有评论数据
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Comments",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (comments.isEmpty()) {
            Text(
                text = "No comments yet.",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .height(300.dp) // 设置评论区域的固定高度
                    .padding(8.dp) // 添加边距
            ) {
                items(comments) { comment ->
                    CommentItem(comment = comment)
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {

    val formattedDate = comment.timestamp?.toDate()?.let { date ->
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date) // ✅ 格式化日期
    } ?: "Unknown"

    // 使用 Column 或 Row 布局显示评论内容
    Column(modifier = Modifier) {
        // 显示用户名和评分
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = comment.userName,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )

            // 显示评分星星
            Row(
                verticalAlignment = Alignment.CenterVertically

            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star Icon",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = comment.rating.toDouble().toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 20.sp
                    )
                )
            }
        }

        // 显示评论内容
        Text(
            text = comment.content,
            modifier = Modifier.padding(top = 4.dp)
        )

        // 显示时间戳
        Text(
            text = "Posted at $formattedDate",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
            color = Color.Gray
        )

//        // 可以显示点赞数（如果需要）
//        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
//            IconButton(onClick = { /* Handle like action */ }) {
//                Icon(
//                    imageVector = Icons.Filled.Favorite,
//                    contentDescription = "Like",
//                    tint = Color.Red
//                )
//            }
//            Text(text = "${comment.likes} likes", style = MaterialTheme.typography.bodySmall)
//        }
    }
}
