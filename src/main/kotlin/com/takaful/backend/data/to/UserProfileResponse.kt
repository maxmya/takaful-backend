package com.takaful.backend.data.to

data class UserProfileResponse(
        val id: Int,
        val phone: String,
        val fullName: String,
        val pictureUrl: String,
        val token: String
)