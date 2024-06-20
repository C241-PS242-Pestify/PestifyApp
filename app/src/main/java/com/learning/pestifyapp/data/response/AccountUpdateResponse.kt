package com.learning.pestifyapp.data.response

import com.google.gson.annotations.SerializedName
import com.learning.pestifyapp.data.model.user.UserData

data class AccountUpdateResponse(
	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("updatedUser")
	val updatedUser: UserData? = null
)
