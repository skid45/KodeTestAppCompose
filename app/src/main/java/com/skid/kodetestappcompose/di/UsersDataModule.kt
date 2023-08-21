package com.skid.kodetestappcompose.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skid.users.data.remote.UserService
import com.skid.users.data.repositories.UserRepositoryImpl
import com.skid.users.data.utils.LocalDateDeserializer
import com.skid.users.domain.repositories.UserRepository
import com.skid.users.domain.usecases.GetFilteredAndSortedUsersUseCase
import com.skid.users.domain.usecases.GetUserByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UsersDataModule {

    @Provides
    fun provideBaseUrl() = "https://stoplight.io/mocks/kode-education/trainee-test/25143926/"

    @Provides
    fun provideLocalDateDeserializer() = LocalDateDeserializer()

    @Provides
    @Singleton
    fun provideGson(localDateDeserializer: LocalDateDeserializer): Gson =
        GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, localDateDeserializer)
            .create()

    @Provides
    @Singleton
    fun provideUserService(gson: Gson, baseUrl: String): UserService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(
        userService: UserService,
        @ApplicationContext context: Context,
    ): UserRepository = UserRepositoryImpl(userService, context)

    @Provides
    fun provideGetFilteredAndSortedUsersUseCase(
        userRepository: UserRepository,
    ): GetFilteredAndSortedUsersUseCase = GetFilteredAndSortedUsersUseCase(userRepository)

    @Provides
    fun provideGetUserByIdUseCase(
        userRepository: UserRepository,
    ): GetUserByIdUseCase = GetUserByIdUseCase(userRepository)

}