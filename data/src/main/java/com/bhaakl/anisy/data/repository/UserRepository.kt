package com.bhaakl.anisy.data.repository

import com.bhaakl.anisy.data.datasource.local.dao.UserQuery
import com.bhaakl.anisy.data.datasource.local.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserQuery){

    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    fun getUserById(userId: Int): Flow<User> = userDao.getUserById(userId)

    fun getUserByEmail(email: String): Flow<User?> = userDao.getUserByEmail(email)

    suspend fun updateLike(email: String, isLiked: Boolean) {
        userDao.updateIsLiked(email, isLiked)
    }

    suspend fun toggleLike(email: String) : Boolean {
        val user = getUserByEmail(email).single()
        val currentIsLiked = user?.isLiked
        if (currentIsLiked != null) {
            userDao.updateIsLiked(email, !currentIsLiked)
        }
        return user?.isLiked ?: false
    }
}