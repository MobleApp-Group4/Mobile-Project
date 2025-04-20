package com.example.todo.ui.components

import android.icu.text.SimpleDateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo.model.Order
import com.example.todo.viewmodel.UserViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderList(
    allOrders:List<Order>,
    userViewModel:UserViewModel
){
    LazyColumn {
        items(allOrders, key = { it.orderId }) { order ->
            var expanded by remember { mutableStateOf(false) }
            var selectedStatus by remember { mutableStateOf(order.status) }
            val statusOptions = listOf("Pending","In Progress", "Completed","Cancelled")
            val formattedDate = order.createdAt.toDate().let { date ->
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date) // ✅ 格式化日期
            } ?: "Unknown"

            var isEditingNote by remember { mutableStateOf(false) }
            var noteText by remember { mutableStateOf(order.note ?: "") }
            var showDetails by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),

                ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "Order ID: ${order.orderId}")
                        Box{
                            TextButton(onClick = { expanded = true }) {
                                Text(text = selectedStatus)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                statusOptions.forEach { status ->
                                    DropdownMenuItem(text = { Text(status) }, onClick = {
                                        selectedStatus = status
                                        expanded = false
                                        userViewModel.updateOrderStatus(order.userId, order.orderId, status)  // 更新状态
                                    })
                                }
                            }
                        }
                    }

                    Text(text = "Created At: $formattedDate")
                    Spacer(modifier = Modifier.height(4.dp))
                    order.orderItems.forEach { item ->
                        Text(text = "- ${item.title} x ${item.quantity}")
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        TextButton(onClick = { showDetails = !showDetails }) {
                            Text(text = if (showDetails) "Hide Details" else "Show Details")
                        }

                        IconButton(onClick = {
                            userViewModel.removeAdminOrder(order.userId,order.orderId) // 假设你有一个删除函数
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Item",
                                tint = Color.White
                            )
                        }
                    }

                    AnimatedVisibility(visible = showDetails) {
                        Column {
                            Text("Address: ${order.address}")
                            Text("Phone: ${order.phoneNumber}")
                            Text("Time: ${order.selectedDate}  ${order.timeSlot}")
                            // Edit Note
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Note")
                                TextButton(onClick = {
                                    if (isEditingNote){
                                        userViewModel.updateOrderNote(order.userId, order.orderId, noteText)
                                    }
                                    isEditingNote = !isEditingNote
                                }) {
                                    Text(text = if (isEditingNote) "Save" else "Edit")
                                }
                            }

                            // 备注内容显示或编辑
                            if (isEditingNote) {
                                OutlinedTextField(
                                    value = noteText,
                                    onValueChange = { noteText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("Enter your note") },
                                    singleLine = false,

                                    )
                            } else {
                                Text(text = noteText)
                                Spacer(modifier = Modifier.height(4.dp))
                            }


                        }
                    }
                }
            }
        }
    }
}
