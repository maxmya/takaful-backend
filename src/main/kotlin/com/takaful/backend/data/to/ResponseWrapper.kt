package com.takaful.backend.data.to

data class ResponseWrapper(
        val success: Boolean,
        val message: String,
        val data: Any?
)