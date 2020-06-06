package com.ionix.shelltest.services

import com.ionix.shelltest.model.DataUser
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ServicesJSONPH
{
    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("users")
    fun addMe(@Body userData: DataUser.UserInfo):
            Observable<DataUser.UserObj>


    companion object {
        fun create(): ServicesJSONPH {

            val retrofit: Retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build()

            return retrofit.create(ServicesJSONPH::class.java)
        }
    }
}