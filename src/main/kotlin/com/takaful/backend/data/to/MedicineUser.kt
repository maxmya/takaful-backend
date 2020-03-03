package com.takaful.backend.data.to

import javax.persistence.Column

data class MedicineUser
(
        val id: Int,
val username: String,
val phone: String,
val fullName: String,
val pictureUrl: String
)