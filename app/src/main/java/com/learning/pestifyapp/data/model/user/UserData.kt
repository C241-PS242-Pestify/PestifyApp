package com.learning.pestifyapp.data.model.user

import com.google.gson.annotations.SerializedName

data class UserData(
    val token: String?,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("profile_Photo")
    var profilePhoto: String? = null,

    val isLogin: Boolean
)

