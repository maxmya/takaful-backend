package com.takaful.backend.data.to

data class Medications(
        val id: Int,
        val name: String,
        val lang: Double,
        val lat: Double,
        val imageUrl: String,
        val addressTitle: String,
        val user:MedicineUser,
        val category:MedicineCategory,
        val preserver:Preservations
)