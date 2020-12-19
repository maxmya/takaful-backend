package com.takaful.backend.data.to

import java.sql.Timestamp


data class UserPreservationDTO(
        val id: Int?,
        val medicine: MedicationsDTO?,
        val timestamp: Timestamp?

)