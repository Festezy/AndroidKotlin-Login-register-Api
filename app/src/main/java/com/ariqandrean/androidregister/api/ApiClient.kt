package com.ariqandrean.androidregister.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    var BASE_URL: String = "http://ariqa.teknisitik.com/api/v1/"
    var retrofit: Retrofit? = null

    fun getApiClient(): Retrofit? {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit
    }
}