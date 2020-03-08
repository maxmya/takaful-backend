package com.takaful.backend.data.to

data class UserProfileResponse(
        val id: Int,
        val phone: String,
        val fullName: String,
        val pictureUrl: String,
        val token: String,
        val medications: List<MedicationsDTO>,
        val reportDTOS: List<ReportDTO>,
        val suggestions: List<SuggestionsDTO>,
        val notifications: List<NotificationDTO>,
        val preservations: List<PreservationsDTO>
)