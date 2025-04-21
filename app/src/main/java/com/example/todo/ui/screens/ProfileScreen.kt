package com.example.todo.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.todo.viewmodel.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    // default avatar
    val user by userViewModel.user.collectAsState()
    var isEditing by remember { mutableStateOf(false) }  // 是否在编辑模式

    LaunchedEffect(Unit) {
        userViewModel.getCurrentUserId()?.let { userId ->
            userViewModel.loadUserData(userId)  // 进入页面时加载用户数据
        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                user?.userId?.let { userId ->
                    userViewModel.updateAvatar(userId, it) { success, message ->
                        if (success) {
                            Log.d("ProfileScreen", " Avatar updated")
                        } else {
                            Log.e("ProfileScreen", " Fail: $message")
                        }
                    }
                }
            }
        }
    )
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            email = it.email
            gender = it.gender
            address = it.address
            phoneNumber = it.phoneNumber
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        user?.let { currentUser ->


            Spacer(modifier = Modifier.height(16.dp))


            if (!isEditing){
                ConstraintLayout(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val (avatarRef, nameRef, genderRef, emailRef) = createRefs()
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .clickable { launcher.launch("image/*") }
                            .constrainAs(avatarRef) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                    ) {
                        if (currentUser.avatar.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(currentUser.avatar),
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Avatar",
                                modifier = Modifier.fillMaxSize(),
                                tint = Color.White
                            )
                        }
                    }

                    // **名字**
                    Text(
                        text = name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.constrainAs(nameRef) {
                            start.linkTo(avatarRef.end, margin = 16.dp)
                            top.linkTo(avatarRef.top)
                        }
                    )

                    // **性别 (带符号, 圆角背景)**
                    val genderSymbol = when (gender) {
                        "Male" -> "♂️"
                        "Female" -> "♀️"
                        "Other" -> "⚧️"
                        else -> "❓"
                    }
                    Box(
                        modifier = Modifier
                            .constrainAs(genderRef) {
                                start.linkTo(avatarRef.end, margin = 16.dp)
                                top.linkTo(nameRef.bottom, margin = 4.dp)
                            }
                    ) {
                        Text(
                            text = genderSymbol,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // **邮箱**
                    Text(
                        text = "📧 $email",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.constrainAs(emailRef) {
                            start.linkTo(avatarRef.end, margin = 16.dp)
                            top.linkTo(genderRef.bottom, margin = 4.dp)
                        }
                    )


                }
                Spacer(modifier = Modifier.height(16.dp))
                // "Edit"
                Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }

            }else{
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable {
                            launcher.launch("image/*") }
                ) {
                    if (currentUser.avatar.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(currentUser.avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Avatar",
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()

                )

                Spacer(modifier = Modifier.height(8.dp))
                // email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                var expanded by remember { mutableStateOf(false) }
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { expanded = true }
//                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
//
//
//                ) {
//                    Text(text = gender)
//                }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it } // 自动管理展开/收起
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true, // 禁止手动输入
                        label = { Text("Gender") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor() // 确保 DropdownMenu 贴合 OutlinedTextField
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Male", "Female", "Other").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Phone Number
                 OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )
                Spacer(modifier = Modifier.height(16.dp))

                //Save
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Button(
                        onClick = {
                            isEditing = false // 退出编辑模式，不保存修改
                        },
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val updatedUserData = currentUser.copy(
                                name = name,
                                email = email,
                                gender = gender,
                                address = address,
                                phoneNumber = phoneNumber,
                                avatar = currentUser.avatar
                            )
                            userViewModel.updateUser(updatedUserData) { success, message ->
                                if (success) {
                                    isEditing = false
                                    Log.d("ProfileScreen", "Saved")
                                } else {
                                    Log.e("ProfileScreen", "Fail: $message")
                                }
                            }
                        },
                    ) {
                        Text("Save")
                    }
                }


            }

        }
    }
}
