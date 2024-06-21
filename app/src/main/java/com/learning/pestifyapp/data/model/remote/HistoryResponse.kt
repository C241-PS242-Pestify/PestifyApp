package com.learning.pestifyapp.data.model.remote

import com.google.gson.annotations.SerializedName
import com.learning.pestifyapp.data.model.historydata.HistoryData

data class HistoryResponse(
    val status: String,
    val data: HistoryData
)
data class ListHistoryResponse(
    @SerializedName("data")
    val data: List<HistoryData>? = null,
    @SerializedName("status")
    val status: String? = null
)