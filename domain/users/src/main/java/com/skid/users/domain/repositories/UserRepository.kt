package com.skid.users.domain.repositories

import com.skid.users.domain.model.UserDetailsItem
import com.skid.users.domain.model.UserItem

interface UserRepository {

    suspend fun getUsers(refresh: Boolean): Result<List<UserItem>>

    suspend fun getUserById(id: String): Result<UserDetailsItem>
}