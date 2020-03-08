package com.takaful.backend.data.to

import javax.persistence.Column

data class MedicineCategory (
    val id: Int,
    val name: String,
    val imageUrl: String
)