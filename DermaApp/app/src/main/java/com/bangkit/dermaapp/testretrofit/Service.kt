package com.bangkit.dermaapp.testretrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface Service {
    @FormUrlEncoded
    @Headers("Content-Type: multipart/form")
    @POST("skin-function")
    fun uploadTestImageImgur(
        @Field("image") image: String?
    ): Call<ResponseTest2>
}