package com.ariqandrean.androidregister.api

import com.ariqandrean.androidregister.response.DefaultResponse
import com.ariqandrean.androidregister.response.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email:String,
        @Field("password") password:String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password: String
    ): Call<DefaultResponse>

    @GET("categories")
    fun getCategories(@Header("Authorization") autHeader: String) : Call<JsonObject>

    @GET("products/searchByCategory/{categoryId}")
    fun getProductByCategory(@Header("Authorization") autHeader: String,
                            @Path("categoryId") id: Int) : Call<JsonObject>
}