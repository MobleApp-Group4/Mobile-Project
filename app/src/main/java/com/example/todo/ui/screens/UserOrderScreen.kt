package com.example.todo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserOrderScreen(
    userViewModel: UserViewModel,
    modifier: Modifier,
    navController: NavController,
    userId : String
) {

    LaunchedEffect(userId) {
        userViewModel.fetchOrders(userId)

    }

    val orders by userViewModel.orders.collectAsState(emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
//        Text(text = "My Orders", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(orders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),

                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Order ID: ${order.orderId}")
                        Text(text = "Status: ${order.status}")
                        Text(text = "Created At: ${order.createdAt}")
                        Spacer(modifier = Modifier.height(4.dp))
                        order.orderItems.forEach { item ->
                            Text(text = "- ${item.title} x ${item.quantity}")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Note: ${order.note}")

                    }
                }
            }
        }
    }
}
