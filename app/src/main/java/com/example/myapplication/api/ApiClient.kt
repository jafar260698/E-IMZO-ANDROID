package com.example.myapplication.api

import com.example.myapplication.model.DeepLinkResponse
import com.example.myapplication.model.StatusModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiClient {

    @POST
    suspend fun getDeepLink(@Url url: String): Response<DeepLinkResponse>

    @POST
    suspend fun checkStatus(
        @Query("documentId")
        documentId: String,
        @Url url: String,
    ): Response<StatusModel>


    companion object {
        var BASE_URL = "https://my.soliq.uz/"

        fun create() : ApiClient {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()
            return retrofit.create(ApiClient::class.java)

        }

    }
}