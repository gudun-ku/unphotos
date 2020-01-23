package com.beloushkin.unphotos.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.beloushkin.unphotos.model.Photo
import com.beloushkin.unphotos.model.UnsplashApiService
import kotlinx.coroutines.*

import ru.gildor.coroutines.retrofit.*

class UnsplashViewModel(application: Application):AndroidViewModel(application)
{

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val photos by lazy { MutableLiveData<List<Photo>>() }
    val loadError by lazy {MutableLiveData<Boolean>()}
    val isLoading by lazy { MutableLiveData<Boolean>()}

    private val apiService = UnsplashApiService()

    fun refresh() {
        isLoading.value = true
        getPhotos()
    }

    private fun getPhotos() {
        coroutineScope.launch {
            val result: Result<List<Photo>> = apiService.getPhotos().awaitResult()

            //Result.Ok and Result.Error both implement ResponseResult
            if (result is ResponseResult) {
                if (result.response.isSuccessful){
                    val loadedPhotos = result.getOrNull()
                    if (loadedPhotos.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            isLoading.value = false
                            loadError.value = true
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            photos.value = loadedPhotos
                            isLoading.value = false
                            loadError.value = false
                        }
                    }
                }
            }

            //Result.Error and Result.Exception implement ErrorResult
            if (result is ErrorResult) {
                // Here we have  access to `exception` property of result
                Log.d("M_UnsplashViewModel", result.exception.localizedMessage)
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    loadError.value = true
                }
            }
        }
    }

    override fun onCleared() {
        photos.value = null
        super.onCleared()
    }
}