package com.learning.pestifyapp.data.model.remote

import com.google.gson.annotations.SerializedName

data class PespediaResponse(

	@field:SerializedName("PespediaResponse")
	val pespediaResponse: List<PespediaResponseItem>
)

data class PespediaResponseItem(

	@field:SerializedName("additionalDescription2")
	val additionalDescription2: String,

	@field:SerializedName("additionalDescription1")
	val additionalDescription1: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("picture")
	val picture: String
)
