package com.learning.pestifyapp.data.model.remote

import com.google.gson.annotations.SerializedName
import com.learning.pestifyapp.data.model.pestdata.PestData

data class PestResponse(
    @SerializedName("data")
    val data: PestData? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: String? = null
)

data class SavePredictionResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class Data(
    @field:SerializedName("pestId")
    val pestId: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null
)