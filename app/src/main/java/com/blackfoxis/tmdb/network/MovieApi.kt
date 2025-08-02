package com.blackfoxis.tmdb.network

import androidx.compose.runtime.getValue
import com.blackfoxis.tmdb.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApi {
    val retrofitService: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}