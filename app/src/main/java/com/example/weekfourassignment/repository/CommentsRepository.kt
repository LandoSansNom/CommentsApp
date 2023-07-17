package com.example.weekfourassignment.repository

import com.example.weekfourassignment.model.CommentModel
import com.example.weekfourassignment.remote.ApiService
import com.example.weekfourassignment.remote.CommentApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CommentsRepository(private val apiService: ApiService) {
    suspend fun getComment(id: Int): Flow<CommentApiState<CommentModel>> {
        return flow {

            // get the comment Data from the api
            val comment=apiService.getComments(id)

            // Emit this data wrapped in
            // the helper class [CommentApiState]
            emit(CommentApiState.success(comment))
        }.flowOn(Dispatchers.IO)
    }
}