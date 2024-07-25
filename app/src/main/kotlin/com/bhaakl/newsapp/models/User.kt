package com.bhaakl.newsapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "class_name") val className: String? = null,
    @ColumnInfo(name = "birthday") val birthday: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "role") val role: Role,
    @ColumnInfo(name = "is_liked") var isLiked: Boolean = false
)