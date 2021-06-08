package com.bangkit.dermaapp.testretrofit

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("severity")
	val severity: String,

	@field:SerializedName("disease")
	val disease: String
)