package com.beloushkin.unphotos.model

import com.beloushkin.unphotos.util.RetrofitClient
import retrofit2.Call


class UnsplashApiService {

    private val BASE_URL = "https://api.unsplash.com"

    private val retrofitClient = RetrofitClient.getApiClient(BASE_URL)
    private val api = retrofitClient!!.create(UnsplashApi::class.java)

    fun getPhotos(): Call<List<Photo>> = api.getPhotos()

    fun getPhoto(id: String): Call<Photo> = api.getPhoto(id)

}