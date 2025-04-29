package com.example.myapplication.api


import com.example.myapplication.util.BooleanDeserializer

import com.google.gson.GsonBuilder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object RetrofitClient {
    private const val BASE_URL = "https://metal-cable-458215-n5.as.r.appspot.com"

    // Create a custom Gson instance with the BooleanDeserializer
    val gson = GsonBuilder()
        .registerTypeAdapter(Boolean::class.java, BooleanDeserializer()) // Ensure this is correct
        .create()

    // Add logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configure OkHttpClient to limit redirects
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .followRedirects(true) // Ensure this is set to true if you want to follow redirects
        .followSslRedirects(true)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson)) // Ensure this uses the custom Gson instance
        .build()

    // Expose the ApiService instance
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}