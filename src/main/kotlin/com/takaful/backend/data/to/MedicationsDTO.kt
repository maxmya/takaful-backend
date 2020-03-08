package com.takaful.backend.data.to

data class MedicationsDTO(
        val id: Int,
        val name: String,
        val lang: Double,
        val lat: Double,
        val imageUrl: String,
        val addressTitle: String,
        val userDTO: MedicineUserDTO,
        val categoryDTO: MedicineCategoryDTO,
        val preserver: PreservationsDTO
)