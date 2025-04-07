package com.example.todo.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    navController: NavController
) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current // 获取当前 Context



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isLogin) "Sign In" else "Sign Up",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            trailingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            trailingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    if (isLogin) {
                        // 调用登录方法
                        userViewModel.loginUser(email, password) { success, error ->
//                            coroutineScope.launch{
//                                snackbarHostState.showSnackbar(
//                                    if (success) "Login Successful!" else "Login Failed!"
//                                )
//                            }
                            if (success){
                                Toast.makeText(
                                    context,
                                    "Login Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true } // 清空登录栈
                                }
                            }else{
                                Toast.makeText(
                                    context,
                                    "Login Failed: $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    } else {
                        // 调用注册方法
                        userViewModel.registerUser(email, password) { success, error ->
//                            coroutineScope.launch{
//                                snackbarHostState.showSnackbar(
//                                    if (success) "Logup Successful!" else "Logup Failed!"
//                                )

                            if (success){
                                isLogin = true
                                Toast.makeText(
                                    context,
                                    "Registration Successful! Please Log In.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }else{
                                Toast.makeText(
                                    context,
                                    "Registration Failed: $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                } else {
//                    coroutineScope.launch {
//                        snackbarHostState.showSnackbar("Email and Password cannot be empty!")
//                    }
                    Toast.makeText(context, "Email and Password cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLogin) "Sign in" else "Sign Up")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextButton(
                onClick = { isLogin = !isLogin } // 点击切换
            ) {
                Text(text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Sign In")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                userViewModel.signInWithGoogle { success, message ->
                    if (!success) {
                        Toast.makeText(context, "Google fail to sign in: $message", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Login Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true } // 清空登录栈
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Google Sign In")
        }
//
//        Button(
//            onClick = { signInWithGoogle(context) }, // 触发 Google 登录
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDB4437)), // Google 经典红色
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_google_logo), // Google Logo
//                contentDescription = "Google Sign In",
//                tint = Color.White
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = "Sign in with Google", color = Color.White)
//        }
    }
}