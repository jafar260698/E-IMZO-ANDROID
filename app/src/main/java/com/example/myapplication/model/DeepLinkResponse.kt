package com.example.myapplication.model

data class DeepLinkResponse(
    val status: Int,
    val message: String,
    val siteId: String,
    val documentId: String,
    val challange: String,
)