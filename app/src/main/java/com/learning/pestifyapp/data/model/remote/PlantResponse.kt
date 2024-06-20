package com.learning.pestifyapp.data.model.remote

import com.google.gson.annotations.SerializedName

data class PlantResponse(

	@field:SerializedName("PlantResponse")
	val plantResponse: List<PlantResponseItem>
)

data class PlantResponseItem(

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("picture")
	val picture: String
)
