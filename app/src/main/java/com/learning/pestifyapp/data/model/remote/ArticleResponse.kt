package com.learning.pestifyapp.data.model.remote

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
	@field:SerializedName("Response")
	val response: List<ArticleResponseItem>
)

data class ArticleResponseItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("tags")
	val tags: String
)
