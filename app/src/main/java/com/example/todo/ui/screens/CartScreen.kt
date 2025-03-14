package com.example.todo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.viewmodel.CartViewModel

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = viewModel()
) {
    Text(text = "Cart", fontSize = 24.sp, fontWeight = FontWeight.Bold)

//    val cartItems = cartViewModel.cartItems
//    Column(modifier = modifier.padding(16.dp)) {
//        LazyColumn {
//            items(cartItems) { item ->
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(text = item.name, fontSize = 18.sp)
//                    Text(text = "${item.price} $", fontSize = 18.sp)
//                    IconButton(onClick = { cartViewModel.removeItem(item) }) {
//                        Icon(Icons.Default.Delete, contentDescription = "Remove")
//                    }
//                }
//            }
//        }
//
//        Button(onClick = { }) {
//            Text("提交订单")
//        }

//    }
}

