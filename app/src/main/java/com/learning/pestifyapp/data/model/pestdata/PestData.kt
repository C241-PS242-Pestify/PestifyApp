package com.learning.pestifyapp.data.model.pestdata

import com.google.gson.annotations.SerializedName

data class PestData(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("pest_name")
    val pestName: String? = null,

    @field:SerializedName("pest_description")
    val pestDescription: String? = null,

    @field:SerializedName("pest_cause")
    val pestCause: String? = null,

    @field:SerializedName("pest_effect")
    val pestEffect: String? = null,

    @field:SerializedName("Solution")
    val solution: String? = null,

    @field:SerializedName("confidenceScore")
    val confidenceScore: Double? = null,

    @field:SerializedName("additional_image")
    val additionalImage: String? = null,
)
