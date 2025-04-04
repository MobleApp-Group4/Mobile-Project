package com.example.todo.ui.appbars

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Badge
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.example.todo.viewmodel.UserViewModel

@Composable
fun OrdersTabBar(
    userViewModel:UserViewModel,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabTitles = listOf("All Orders", "New Orders") // 可以添加更多分类
    val newOrders by userViewModel.newOrders.collectAsState()

    TabRow(selectedTabIndex = selectedTabIndex) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title)
                    if (index == 1 && newOrders.isNotEmpty()) { // 仅在 "New Orders" 里显示数量
                        Badge { Text("${newOrders.size}") }
                    }
                }
            }
        }
    }
}

