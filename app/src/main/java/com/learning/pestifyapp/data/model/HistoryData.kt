package com.learning.pestifyapp.data.model

import com.google.gson.annotations.SerializedName
import com.learning.pestifyapp.data.model.pestdata.PestData

data class HistoryData(
    @field:SerializedName("pest")
    val pest: PestData? = null,

    @field:SerializedName("pestId")
    val pestId: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,
)
