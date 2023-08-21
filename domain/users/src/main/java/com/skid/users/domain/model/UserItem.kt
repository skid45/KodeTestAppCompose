package com.skid.users.domain.model

import java.io.Serializable
import java.time.LocalDate

data class UserItem(
    val id: String,
    val avatarUrl: String,
    val name: String,
    val birthday: LocalDate,
    val department: String,
    val userTag: String,
    val phone: String,
    val monthDayOfBirthday: String,
): UserListItem(), Serializable