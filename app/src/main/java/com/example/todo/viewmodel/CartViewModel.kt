package com.example.todo.viewmodel

import android.view.MenuItem
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    var cartItems = mutableStateListOf<MenuItem>()  // 直接用可变列表
        private set

    fun addItem(item: MenuItem) {
        cartItems.add(item)  // 添加到列表
    }

    fun removeItem(item: Int) {
        cartItems.removeAt(item)  // 从列表移除
    }

    fun clearCart() {
        cartItems.clear()  // 清空购物车
    }
}

