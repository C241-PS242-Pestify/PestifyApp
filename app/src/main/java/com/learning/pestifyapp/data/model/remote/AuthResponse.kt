package com.learning.pestifyapp.data.model.remote

import com.google.gson.annotations.SerializedName
import com.learning.pestifyapp.data.model.user.UserData

data class RegisterResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("updatedUser")
    val updatedUser: UserData? = null
)
data class LoginResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("user")
    val user: UserData? = null,

    @field:SerializedName("token")
    val token: String? = null
)

data class LogoutResponse(
    @field:SerializedName("message")
    val message: String? = null
)
