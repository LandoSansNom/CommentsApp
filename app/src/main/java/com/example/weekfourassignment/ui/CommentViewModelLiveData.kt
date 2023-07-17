package com.example.weekfourassignment.ui
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weekfourassignment.model.CommentModel
import com.example.weekfourassignment.remote.CommentApiState
import com.example.weekfourassignment.remote.Status
import com.example.weekfourassignment.repository.CommentsRepository
import com.example.weekfourassignment.utils.AppConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CommentsLiveDataViewModel : ViewModel() {

    // Create a Repository and pass the api
    // service we created in AppConfig file
    private val repository = CommentsRepository(
        AppConfig.ApiService()
    )

    // Use MutableLiveData instead of MutableStateFlow
    private val _commentState = MutableLiveData<CommentApiState<CommentModel>>()
    val commentState: LiveData<CommentApiState<CommentModel>> get() = _commentState



    init {
        // Initiate a starting
        // search with comment Id 1
        getNewComment(1)
    }


    fun getNewComment(id: Int) {
        _commentState.value = CommentApiState.loading()

        viewModelScope.launch {
            repository.getComment(id)
                .catch {
                    _commentState.value = CommentApiState.error(it.message.toString())
                }
                .collect {
                    _commentState.value = CommentApiState.success(it.data)
                }
        }
    }
}
