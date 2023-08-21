package com.skid.users.domain.model

data class UserDetailsItem(
    val id: String,
    val avatarUrl: String,
    val name: String,
    val userTag: String,
    val department: String,
    val birthday: String,
    val age: String,
    val phone: String,
)
