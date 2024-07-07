package com.example.newsapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteModel(
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String?
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}