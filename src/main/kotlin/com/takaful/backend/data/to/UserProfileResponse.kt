package com.takaful.backend.data.to

data class UserProfileResponse(
        val id:Int,
        val phone: String,
        val fullName: String,
        val pictureUrl: String,
        val medications: List<Medications>,
        val reports: List<Report>,
        val suggestions: List<Suggestions>,
        val notifications: List<Notifications>
)