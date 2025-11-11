package com.example.futurescript.data.network.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class LetterDto (
    @SerializedName("id") val id: Long?,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
)

const val json = """{"id":1,"title":"test","content":"test"}"""""
val letter = Gson().fromJson(json, LetterDto::class.java)
