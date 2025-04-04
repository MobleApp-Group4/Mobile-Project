package com.example.todo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.ui.appbars.OrdersTabBar
import com.example.todo.ui.components.AdminOrderList
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AdminOrderScreen(
    modifier: Modifier,
    userViewModel: UserViewModel,
    ) {
    val allOrders by userViewModel.allOrders.collectAsState()
    val newOrders by userViewModel.newOrders.collectAsState() // 获取新订单
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 记录当前选中的 Tab


    LaunchedEffect(selectedTabIndex) {
        userViewModel.getAllOrders()
        userViewModel.getNewOrders()

    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
//        Text(text = "My Orders", style = MaterialTheme.typography.headlineMedium)
        OrdersTabBar(userViewModel,selectedTabIndex) { index ->
            selectedTabIndex = index // 更新选中的 Tab
        }
        when (selectedTabIndex) {
            0 -> AdminOrderList(allOrders,userViewModel) // 显示所有订单
            1 -> AdminOrderList(newOrders,userViewModel) // 显示新订单
        }
    }
}
