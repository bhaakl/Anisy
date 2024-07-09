package com.example.newsapp.viewmodel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Role
import com.example.newsapp.models.User
import com.example.newsapp.room.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class MainViewModel(private val userRepo: UserRepository) : ViewModel() {

    // Register
    fun registerUser(
        name: String,
        className: String?,
        birthday: String,
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
                        birthday = birthday,
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

    private fun initializeAesKey(): SecretKey {
//        val aesKey: SecretKey
        val keyGenerator = KeyGenerator.getInstance("AES")
        val secureRandom = SecureRandom()
        keyGenerator.init(256, secureRandom) // Use a valid AES key size (128, 192, or 256 bits)
        return keyGenerator.generateKey()
    }

    private fun encrypt(textToEncrypt: String): String {
        val secureRandom = SecureRandom()
        val aesKey = initializeAesKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16) // Generate a random IV for each encryption
        secureRandom.nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec)
        val encryptedBytes = cipher.doFinal(textToEncrypt.toByteArray())
        // Concatenate IV and encrypted data for decryption
        val combinedData = iv + encryptedBytes
        return Base64.encodeToString(combinedData, Base64.DEFAULT)
    }

    private fun decrypt(encryptedText: String): String {
        val aesKey = initializeAesKey()
        val combinedData = Base64.decode(encryptedText, Base64.DEFAULT)
        val iv = combinedData.copyOfRange(0, 16) // Extract IV
        val encryptedBytes =
            combinedData.copyOfRange(16, combinedData.size) // Extract encrypted data
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    fun getUserByEmail(email: String): Flow<User?> {
        return userRepo.getUserByEmail(email)
    }

    // users data
    fun getUserData(email: String): Flow<Map<String, String>?> {
        return userRepo.getUserByEmail(email).map { user ->
            if (user != null) {
                val splitForSlug = user.name.split(" ")
                val userData = mutableMapOf<String, String>()
                userData["slugProfile"] = if (splitForSlug.size >= 2) {
                    val firstSymbol = splitForSlug[0].first()
                    val secondSymbol = splitForSlug[1].first()
                    "$firstSymbol$secondSymbol"
                } else {
                    if (splitForSlug.size == 1) splitForSlug[0].first().toString()
                    else ""
                }
                userData["fio"] = user.name
                userData["className"] = user.className ?: ""
                userData["birthday"] = user.birthday
                userData["role"] = user.role.toString()
                userData
            } else null
        }.flowOn(Dispatchers.IO)
    }

    // like
    fun toggleLike(email: String) : Boolean {
        var isLiked = false
        viewModelScope.launch {
            isLiked = userRepo.toggleLike(email)
        }
        return isLiked
    }

    fun getUserId(email: String): Int? {
        var userId: Int? = null
        viewModelScope.launch {
            userId = userRepo.getUserIdByEmail(email)
            if (userId != null) {
                val user = userRepo.getUserById(userId!!).single()
                Log.e("MainViewModel.getUserId", "User -> {${user.name}; ${user.isLiked}; $userId")
            } else
                Log.e("MainViewModel.getUserId", "User with email $email not found")
        }
        return userId
    }

    //Comment
}