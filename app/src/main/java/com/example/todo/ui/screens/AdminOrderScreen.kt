package com.example.todo.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
            0 -> {
                var selectedStatus by remember { mutableStateOf("All") }
                var searchText by remember { mutableStateOf("") }
                var expanded by remember { mutableStateOf(false) }

                val statusOptions = listOf("All", "Pending", "In Progress", "Completed", "Cancelled")

                val filteredOrders = allOrders.filter { order ->
                    val matchesStatus = selectedStatus == "All" || order.status == selectedStatus
                    val matchesSearch = order.orderId.contains(searchText, ignoreCase = true)
                    matchesStatus && matchesSearch
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    // 状态筛选按钮（带图标）
                    Box {
                        TextButton(
                            onClick = { expanded = true },
                        ) {
                            Text(text = selectedStatus)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            statusOptions.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status) },
                                    onClick = {
                                        selectedStatus = status
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // 搜索框
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Search Order ID") },
                        trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                AdminOrderList(filteredOrders,userViewModel)
            }
            1 -> {
                var searchText by remember { mutableStateOf("") }
                val filteredOrders = newOrders.filter { order ->
                    order.orderId.contains(searchText, ignoreCase = true)
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Search Order ID") },
                        trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                AdminOrderList(filteredOrders,userViewModel)
            }
        }
    }
}

