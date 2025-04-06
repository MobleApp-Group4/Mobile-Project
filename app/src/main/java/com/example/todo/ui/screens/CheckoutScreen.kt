package com.example.todo.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FilterChip
import androidx.compose.material3.TextButton
import java.util.*



@Composable
fun CheckoutScreen(
    navController: NavController,
    modifier: Modifier,
    userViewModel: UserViewModel = viewModel()
) {

//    val userInfo by viewModel.userInfo.observeAsState()
    val cartItems by userViewModel.cartItems.collectAsState(emptyList())
    val userInfo by userViewModel.user.collectAsState()

    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedTimeSlot by remember { mutableStateOf("") } // 默认选择午餐时段
    var selectedDate by remember { mutableStateOf("Select Date") } // 默认日期
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    LaunchedEffect(userId) {
        if (userId != null){
            userViewModel.getCartItems(userId)
            userViewModel.loadUserData(userId)
        }
    }

    LaunchedEffect(userInfo) {
        userInfo?.let {
            address = it.address ?: ""
            phoneNumber = it.phoneNumber ?: ""
        }
    }

    // DatePicker Dialog state
    val context = LocalContext.current
    val datePickerDialog = remember { mutableStateOf<DatePickerDialog?>(null) }

    // Function to show DatePicker
    fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time
                selectedDate = dateFormatter.format(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.value = datePicker
        datePicker.show()
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Confirm Your Order", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // 地址输入框
        Text("Address", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        TextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your address") }
        )
        Spacer(modifier = Modifier.height(4.dp))
        // 电话输入框
        Text("Phone Number", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your phone number") }
        )
        Spacer(modifier = Modifier.height(4.dp))
        // 日期选择
        Text("Select Date", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        TextButton(onClick = { showDatePicker() }) {
            Text(selectedDate)
        }
        // 时间段选择
        Text("Select Time Slot", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf("Lunch", "Dinner").forEach { timeSlot ->
//                Button(
//                    onClick = { selectedTimeSlot = timeSlot },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(timeSlot)
//                }
                FilterChip(
                    selected = selectedTimeSlot == timeSlot,
                    onClick = { selectedTimeSlot = timeSlot },
                    label = { Text(timeSlot) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Note", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        TextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Any additional instructions? ") }
        )

        // 购物车商品列表
        LazyColumn {
            items(cartItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "x${item.quantity}",
                        textAlign = TextAlign.End
                        )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 确认订单按钮
        Button(
            onClick = {
                if (userId != null){
                    userViewModel.confirmOrder(userId,address,phoneNumber,selectedTimeSlot,selectedDate,note)
                }
                navController.navigate("orders") // 跳转到订单成功页面
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Order")
        }
    }
}