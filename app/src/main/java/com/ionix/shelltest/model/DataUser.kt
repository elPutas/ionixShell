package com.ionix.shelltest.model

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName


object DataUser {
    data class Info(val responseCode: String, val description: String, val result: Items)
    data class Items(val items: JsonArray)

    data class UserObj(val id: String, val userId: Number)


    data class UserInfo (
        @SerializedName("name") val userName: String?,
        @SerializedName("email") val userEmail: String?,
        @SerializedName("user_phone") val userPhone: String?
    )
}