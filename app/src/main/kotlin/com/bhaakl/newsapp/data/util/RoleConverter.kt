package com.bhaakl.newsapp.data.util

import androidx.room.TypeConverter
import com.bhaakl.newsapp.domain.model.Role

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