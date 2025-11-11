package com.example.futurescript.data.network.api

import com.example.futurescript.data.network.model.LetterDto
import retrofit2.Response
import retrofit2.http.*

interface FutureScriptApiService {

    @GET("letters")
    suspend fun getLetters(): Response<List<LetterDto>>

    @GET("letters/{id}")
    suspend fun getLetterById(@Path("id") id: Long): Response<LetterDto>

    @POST("letters")
    suspend fun createLetter(@Body letter: LetterDto): Response<LetterDto>

    @PUT("letters/{id}")
    suspend fun updateLetter(
        @Path("id") id: Long,
        @Body letter: LetterDto
    ): Response<LetterDto>

    @DELETE("letters/{id}")
    suspend fun deleteLetter(@Path("id") id: Long): Response<Unit>


}
