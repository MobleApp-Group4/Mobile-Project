package com.example.todo.model

data class Todo(
    //@SerializedName("userId") var userId: Int,  // API 返回字段
    var userId: Int,
    var id: Int,
    var title: String,
    var completed: Boolean
)