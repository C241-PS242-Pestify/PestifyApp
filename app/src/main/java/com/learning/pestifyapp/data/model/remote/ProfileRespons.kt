package com.learning.pestifyapp.data.model.remote

import com.google.gson.annotations.SerializedName
import com.learning.pestifyapp.data.model.user.UserData

data class AccountUpdateResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("updatedUser")
    val updatedUser: UserData? = null
)

data class ProfileResponse(
    @field:SerializedName("user")
    val user: UserData? = null
)

data class UpdateImageResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("updatedUser") val updatedUser: UserData? = null,
)