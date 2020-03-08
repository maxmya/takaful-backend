package com.takaful.backend.data.to


data class MedicineUserDTO(
        val id: Int,
        val username: String,
        val phone: String,
        val fullName: String,
        val pictureUrl: String
)