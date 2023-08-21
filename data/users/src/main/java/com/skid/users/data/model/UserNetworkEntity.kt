package com.skid.users.data.model

import java.time.LocalDate

data class UserNetworkEntity(
    val id: String,
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val department: String,
    val userTag: String,
    val phone: String,
)