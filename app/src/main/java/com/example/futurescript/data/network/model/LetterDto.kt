package com.example.futurescript.data.network.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class LetterDto (
    @SerializedName("id") val id: Long,
    @SerializedName("message") val message: String,
    @SerializedName("date_to_deliver") val deliverAtEpochSec: Long,
    @SerializedName("sent_at") val createdAtEpochSec: Long,
    @SerializedName("is_delivered") val isDelivered: Boolean
)

const val json = """{"id":1,"title":"test","content":"test"}"""""
val letter = Gson().fromJson(json, LetterDto::class.java)
