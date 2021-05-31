package com.ariqandrean.androidregister.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token:String?
)
