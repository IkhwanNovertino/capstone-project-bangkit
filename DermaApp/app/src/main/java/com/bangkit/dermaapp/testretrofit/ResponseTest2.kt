package com.bangkit.dermaapp.testretrofit

import com.google.gson.annotations.SerializedName

data class ResponseTest2(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: String,

	@field:SerializedName("status")
	val status: String
)