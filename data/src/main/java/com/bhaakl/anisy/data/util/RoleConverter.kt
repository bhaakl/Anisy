package com.bhaakl.anisy.data.util

import androidx.room.TypeConverter
import com.bhaakl.anisy.data.datasource.local.entity.Role

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