package com.skid.main_screen.ui

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skid.users.domain.model.Sorting
import com.skid.users.domain.model.UserListItem
import com.skid.users.domain.usecases.GetFilteredAndSortedUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getFilteredAndSortedUsersUseCase: GetFilteredAndSortedUsersUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenUiState())
    val state get() = _state.asStateFlow()

    private var previousStates: Pair<MainScreenUiState?, MainScreenUiState?> = null to null

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.OnQueryChange -> _state.update { it.copy(query = event.query) }
            is MainScreenEvent.OnIsRefreshChange -> _state.update { it.copy(isRefresh = event.isRefresh) }
            is MainScreenEvent.OnPageChange -> _state.update { it.copy(page = event.page) }
            is MainScreenEvent.OnSortByChange -> _state.update { it.copy(sortBy = event.sortBy) }
            is MainScreenEvent.OnWasSkeletonShownChange -> _state.update { it.copy(wasSkeletonShown = event.wasSkeletonShown) }
            is MainScreenEvent.OnNetworkErrorChange -> _state.update { it.copy(networkError = event.networkError) }
        }
    }

    init {
        viewModelScope.launch {
            _state.collect { uiState ->
                previousStates = previousStates.second to uiState
                val result = getFilteredAndSortedUsersUseCase(
                    query = uiState.query,
                    sortBy = uiState.sortBy,
                    refresh = uiState.isRefresh
                )
                Log.d("TAG", "uiState: $uiState")
                if (result.isSuccess) {
                    val userList = result.getOrNull()!!
                    val networkError = if (
                        previousStates.second?.networkError != null &&
                        userList == previousStates.second?.userList &&
                        previousStates.first?.networkError == null
                    ) previousStates.second?.networkError
                    else null

                    _state.update {
                        it.copy(
                            userList = userList,
                            networkError = networkError,
                            isRefresh = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            networkError = result.exceptionOrNull()?.localizedMessage,
                            isRefresh = false
                        )
                    }
                }
            }
        }
    }
}

@Immutable
data class MainScreenUiState(
    val query: String = "",
    val userList: List<UserListItem> = emptyList(),
    val sortBy: Sorting = Sorting.BY_ALPHABET,
    val isRefresh: Boolean = false,
    val page: Int = 0,
    val networkError: String? = null,
    val wasSkeletonShown: Boolean = false,
)

@Immutable
sealed class MainScreenEvent {
    data class OnQueryChange(val query: String) : MainScreenEvent()
    data class OnSortByChange(val sortBy: Sorting) : MainScreenEvent()
    data class OnIsRefreshChange(val isRefresh: Boolean) : MainScreenEvent()
    data class OnPageChange(val page: Int) : MainScreenEvent()
    data class OnWasSkeletonShownChange(val wasSkeletonShown: Boolean) : MainScreenEvent()
    data class OnNetworkErrorChange(val networkError: String?) : MainScreenEvent()
}