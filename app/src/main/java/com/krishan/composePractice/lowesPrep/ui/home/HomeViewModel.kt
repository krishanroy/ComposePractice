package com.krishan.composePractice.lowesPrep.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishan.composePractice.lowesPrep.domain.model.Photo
import com.krishan.composePractice.lowesPrep.domain.model.Todo
import com.krishan.composePractice.lowesPrep.domain.model.User
import com.krishan.composePractice.lowesPrep.domain.repo.PhotosRepository
import com.krishan.composePractice.lowesPrep.domain.repo.TodosRepository
import com.krishan.composePractice.lowesPrep.domain.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val photosRepository: PhotosRepository,
    private val todosRepository: TodosRepository
) : ViewModel() {
    private val _homeMutableStateFlow = MutableStateFlow(HomeScreenUiState())
    val homeStateFlow = _homeMutableStateFlow.asStateFlow()

    init {
        fetchAllData()
    }

    fun fetchAllData() {
        _homeMutableStateFlow.update { homeScreenUiState ->
            homeScreenUiState.copy(
                userUiState = homeScreenUiState.userUiState.copy(loading = true),
                photosUiState = homeScreenUiState.photosUiState.copy(loading = true),
                todosUiState = homeScreenUiState.todosUiState.copy(loading = true)
            )
        }
        viewModelScope.launch {
            supervisorScope {
                val usersDeferred = async { userRepository.fetchUsers() }
                val photosDeferrer = async { photosRepository.fetchPhotos() }
                val todosDeferred = async { todosRepository.fetchTodos() }
                try {
                    val users = usersDeferred.await()
                    val photos = photosDeferrer.await()
                    val todos = todosDeferred.await()

                    _homeMutableStateFlow.update { homeScreenUiState ->
                        homeScreenUiState.copy(
                            userUiState = homeScreenUiState.userUiState.copy(loading = false, users = users),
                            photosUiState = homeScreenUiState.photosUiState.copy(loading = false, photos = photos),
                            todosUiState = homeScreenUiState.todosUiState.copy(loading = false, todos = todos)
                        )
                    }
                } catch (e: Exception) {
                    _homeMutableStateFlow.update { homeScreenUiState ->
                        homeScreenUiState.copy(
                            userUiState = homeScreenUiState.userUiState.copy(
                                loading = false,
                                error = "Something went wrong"
                            ),
                            photosUiState = homeScreenUiState.photosUiState.copy(loading = false),
                            todosUiState = homeScreenUiState.todosUiState.copy(loading = false)
                        )
                    }
                }
            }
        }
    }
}

data class HomeScreenUiState(
    val userUiState: UserUiState = UserUiState(),
    val photosUiState: PhotosUiState = PhotosUiState(),
    val todosUiState: TodosUiState = TodosUiState()
)

data class UserUiState(val loading: Boolean = false, val error: String? = null, val users: List<User> = emptyList())
data class PhotosUiState(val loading: Boolean = false, val error: String? = null, val photos: List<Photo> = emptyList())
data class TodosUiState(val loading: Boolean = false, val error: String? = null, val todos: List<Todo> = emptyList())