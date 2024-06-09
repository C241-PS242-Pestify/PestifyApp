package com.learning.pestifyapp.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: UserData? = null,

	@field:SerializedName("token")
	val token: String? = null
)
