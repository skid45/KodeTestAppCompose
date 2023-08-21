package com.skid.user_details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skid.users.domain.model.UserDetailsItem
import com.skid.users.domain.usecases.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : ViewModel() {

    private val id: String = checkNotNull(savedStateHandle["id"])

    private val _userDetails = MutableStateFlow<UserDetailsItem?>(null)
    val userDetails get() = _userDetails.asStateFlow()

    init {
        viewModelScope.launch {
            _userDetails.value = getUserByIdUseCase(id).getOrNull()
        }
    }
}