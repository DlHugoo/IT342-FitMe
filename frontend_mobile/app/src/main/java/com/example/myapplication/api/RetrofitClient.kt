package com.example.myapplication.api


import com.example.myapplication.util.BooleanDeserializer

import com.google.gson.GsonBuilder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"

    // Create a custom Gson instance with the BooleanDeserializer
    val gson = GsonBuilder()
        .registerTypeAdapter(Boolean::class.java, BooleanDeserializer()) // Ensure this is correct
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson)) // Ensure this uses the custom Gson instance
        .build()

    // Expose the ApiService instance
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}