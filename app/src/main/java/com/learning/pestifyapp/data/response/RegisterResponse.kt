package com.learning.pestifyapp.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("updatedUser")
	val updatedUser: UserData? = null
)


