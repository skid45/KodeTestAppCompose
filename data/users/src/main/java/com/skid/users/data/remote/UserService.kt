package com.skid.users.data.remote

import com.skid.users.data.model.ResponseNetworkEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface UserService {

    @GET("users")
//    @Headers("Prefer: code=200, example=success")
    @Headers("Prefer: code=200, dynamic=true")
//    @Headers("Prefer: code=500, example=error-500")
    suspend fun getUsers(): Response<ResponseNetworkEntity>
}