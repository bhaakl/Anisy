package com.example.newsapp.utils

import androidx.room.TypeConverter
import com.example.newsapp.models.Role

class RoleConverter {
    @TypeConverter
    fun fromRole(role: Role): String {
        return role.name
    }

    @TypeConverter
    fun toRole(roleName: String): Role {
        return Role.valueOf(roleName)
    }
}