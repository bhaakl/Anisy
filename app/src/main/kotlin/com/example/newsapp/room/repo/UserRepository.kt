package com.example.newsapp.room.repo

import com.example.newsapp.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserQuery
) {

    fun getAllUsers(): Flow<List<User>> =userDao.getAllUsers()

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    fun getUserById(userId: Int): Flow<User> = userDao.getUserById(userId)
}