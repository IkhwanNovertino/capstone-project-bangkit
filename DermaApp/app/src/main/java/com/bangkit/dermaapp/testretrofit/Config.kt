package com.bangkit.dermaapp.testretrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Config {

    private const val BASE_URL = "https://asia-southeast2-b21-cap0391.cloudfunctions.net/"
    private var mRetrofit: Retrofit? = null

    fun getTestRetrofit(): Retrofit? {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)

            .build()


        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)

            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return mRetrofit
    }
}