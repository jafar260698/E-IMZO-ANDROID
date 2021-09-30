package com.example.myapplication.api

import com.example.myapplication.model.DeepLinkResponse
import com.example.myapplication.model.StatusModel
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface ServiceApi {

    @POST
    suspend fun getDeepLink(
        @Url url: String
    ): Response<DeepLinkResponse>

    @POST
    suspend fun checkStatus(
        @Url url: String
    ): Response<StatusModel>

}