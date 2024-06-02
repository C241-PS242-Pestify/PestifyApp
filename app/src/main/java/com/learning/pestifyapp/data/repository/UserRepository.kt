package com.learning.pestifyapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.learning.pestifyapp.data.model.UserData
import kotlinx.coroutines.tasks.await

class UserRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
    suspend fun login(email: String, password: String): Result<UserData> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val user = createUserData(firebaseUser)
                saveUserSession(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<UserData> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val user = createUserData(firebaseUser)
                Result.success(user)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun setUsername(username: String):Result<UserData> {
        val firebaseUser = auth.currentUser
        return try {
            if (firebaseUser != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()
                val user = createUserData(firebaseUser)
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun getUserSession(): UserData? {
        return if (isLoggedIn()) {
            val firebaseUser = auth.currentUser
            firebaseUser?.let { createUserData(it) }
        } else {
            null
        }
    }
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun logout() {
        auth.signOut()
        sharedPreferences.edit().clear().apply()
    }

      fun saveUserSession(user: UserData) {
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("userId", user.userId)
            apply()
        }
    }
     fun createUserData(firebaseUser: FirebaseUser): UserData {
        return UserData(
            userId = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            name = firebaseUser.displayName ?: ""
        )
    }
}