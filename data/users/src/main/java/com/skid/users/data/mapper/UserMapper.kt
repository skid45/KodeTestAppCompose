package com.skid.users.data.mapper

import com.skid.users.data.model.UserNetworkEntity
import com.skid.users.domain.model.UserDetailsItem
import com.skid.users.domain.model.UserItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun UserNetworkEntity.toUserItem(): UserItem {
    val formatter = DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("ru"))
    return UserItem(
        id = id,
        avatarUrl = avatarUrl,
        name = "$firstName $lastName",
        birthday = birthday,
        department = department.getDisplayDepartment(),
        userTag = userTag.lowercase(),
        phone = phone,
        monthDayOfBirthday = birthday.format(formatter).dropLastWhile { it == '.' }
    )
}

fun UserNetworkEntity.toUserDetailsItem(): UserDetailsItem {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    val age = birthday.until(LocalDate.now(), ChronoUnit.YEARS)
    val ageString = "$age ${age.getYearsDeclension()}"

    return UserDetailsItem(
        id = id,
        avatarUrl = avatarUrl,
        name = "$firstName $lastName",
        userTag = userTag.lowercase(),
        department = department.getDisplayDepartment(),
        birthday = birthday.format(formatter),
        age = ageString,
        phone = phone
    )
}

private fun Long.getYearsDeclension(): String {
    val mod100 = this % 100
    val mod10 = this % 10
    return when {
        mod100 in 11L..14L -> "лет"
        mod10 == 1L -> "год"
        mod10 in 2L..4L -> "года"
        else -> "лет"
    }
}

private fun String.getDisplayDepartment(): String {
    return when (this) {
        "android" -> "Android"
        "ios" -> "iOS"
        "design" -> "Дизайн"
        "management" -> "Менеджмент"
        "qa" -> "QA"
        "back_office" -> "Бэк-офис"
        "frontend" -> "Frontend"
        "hr" -> "HR"
        "pr" -> "PR"
        "backend" -> "Backend"
        "support" -> "Техподдержка"
        "analytics" -> "Аналитика"
        else -> this
    }
}