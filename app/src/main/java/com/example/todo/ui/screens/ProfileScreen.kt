package com.example.todo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todo.R

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    // 默认头像
    val defaultAvatar = R.drawable.sisi // 使用默认头像图片的资源ID
    // 假设数据
    val username = "Sisi Chen"
    val gender = "Female"
    val birthday = "01 January 1990"

    // Profile 页面布局
    Row (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // 头像显示
        Image(
            painter = painterResource(id = defaultAvatar),
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Column(
            modifier = Modifier.padding(start = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Username: $username", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text(text = "Gender: $gender", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Birthdate: $birthday", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
