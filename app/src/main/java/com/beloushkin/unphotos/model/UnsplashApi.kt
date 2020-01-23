package com.beloushkin.unphotos.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UnsplashApi {

    @GET("photos")
    fun getPhotos(): Call<List<Photo>>

    @GET("photos/{id}")
    fun getPhoto(@Path("id") photoId: String): Call<Photo>
}