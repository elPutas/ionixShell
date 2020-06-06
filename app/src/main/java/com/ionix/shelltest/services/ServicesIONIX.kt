package com.ionix.shelltest.services

import com.ionix.shelltest.model.DataUser
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ServicesIONIX
{
    @GET("test-tecnico/search")
    fun logUser(@Query("rut") rut: String):
            Observable<DataUser.Info>


    companion object {
        fun create(): ServicesIONIX {

            val retrofit:Retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://sandbox.ionix.cl/")
                    .build()

            return retrofit.create(ServicesIONIX::class.java)
        }
    }

}