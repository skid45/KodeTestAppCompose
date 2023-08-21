package com.skid.users.data.repositories

import android.content.Context
import com.skid.users.R
import com.skid.users.data.mapper.toUserDetailsItem
import com.skid.users.data.mapper.toUserItem
import com.skid.users.data.model.UserNetworkEntity
import com.skid.users.data.remote.UserService
import com.skid.users.domain.model.UserDetailsItem
import com.skid.users.domain.model.UserItem
import com.skid.users.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class UserRepositoryImpl(
    private val userService: UserService,
    private val context: Context,
) : UserRepository {

    private var cachingUsers: List<UserNetworkEntity> = emptyList()

    override suspend fun getUsers(refresh: Boolean): Result<List<UserItem>> {
        if (refresh || cachingUsers.isEmpty()) {
            try {
                val response = userService.getUsers()
                if (response.isSuccessful) {
                    cachingUsers = response.body()!!.items
                } else {
                    return Result.failure(Exception(context.getString(R.string.api_error)))
                }
            } catch (e: IOException) {
                return Result.failure(Exception(context.getString(R.string.network_error)))
            } catch (e: Exception) {
                return Result.failure(e)
            }
        }
        return Result.success(cachingUsers.map { it.toUserItem() })
    }

    override suspend fun getUserById(id: String): Result<UserDetailsItem> =
        withContext(Dispatchers.IO) {
            val user = cachingUsers.firstOrNull { it.id == id }
            if (user != null) {
                Result.success(user.toUserDetailsItem())
            } else Result.failure(Exception(context.getString(R.string.user_not_found)))
        }
}