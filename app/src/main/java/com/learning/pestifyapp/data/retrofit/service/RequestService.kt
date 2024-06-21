package com.learning.pestifyapp.data.retrofit.service

import com.google.gson.annotations.SerializedName

data class PhotoUrlRequest(
    @SerializedName("profile_Photo") val profilePhoto: String
)
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class UpdateAccountRequest(
    val username: String?,
    val email: String?,
    val password: String?,
)