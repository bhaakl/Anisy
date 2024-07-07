package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.User
import com.example.newsapp.room.repo.UserQuery
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.util.Base64
import android.util.Log
import com.example.newsapp.R
import com.example.newsapp.models.Role
import com.example.newsapp.room.repo.UserRepository
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MainViewModel @Inject constructor(private val userRepo: UserRepository) : ViewModel() {

    /*fun onEditGradesClick(studentId: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser()
            if (currentUser.role == "Teacher") {
                // Переход на экран редактирования оценок
            } else {
                // Отображение сообщения об ошибке
            }
        }
    }*/
    fun registerUser(
        name: String,
        className: String?,
        email: String,
        password: String,
        role: Role
    ) {
        viewModelScope.launch {
            try {
                val passwordHash = encrypt(password) // Хэшируем пароль
                val user =
                    User(
                        name = name,
                        className = if (role == Role.TEACHER) null else className,
                        email = email,
                        password = passwordHash,
                        role = role
                    )
                userRepo.insertUser(user)
            } catch (e: Exception) {
                Log.e("MainViewModel.registerUser", "Error registering user", e)
            }
        }
    }

    private fun encrypt(textToEncrypt: String): String {
        val key = R.string.api_key.toString()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(key.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encryptedBytes = cipher.doFinal(textToEncrypt.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT).toString()
    }

    private fun decrypt(encryptedText: String): String {
        val key = R.string.api_key.toString()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(key.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
        val decryptBytes = cipher.doFinal(encryptedBytes)
        return String(decryptBytes)
    }
}