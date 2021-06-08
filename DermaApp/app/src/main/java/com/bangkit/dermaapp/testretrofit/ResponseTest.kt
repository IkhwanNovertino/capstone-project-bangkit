package com.bangkit.dermaapp.testretrofit

import com.google.gson.annotations.SerializedName

data class ResponseTest(

	@field:SerializedName("data")
	val data: String,

	@field:SerializedName("success")
	val success: String,

	@field:SerializedName("status")
	val status: String
)