package com.takaful.backend.data.to

data class MedicationCreationForm(
        val name: String,
        val address: String,
        val lang: Double,
        val lat: Double,
        val categoryId: Int,
        val userId: Int
)