package com.skid.users.domain.usecases

import com.skid.users.domain.model.UserDetailsItem
import com.skid.users.domain.repositories.UserRepository

class GetUserByIdUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(id: String): Result<UserDetailsItem> {
        return userRepository.getUserById(id)
    }
}