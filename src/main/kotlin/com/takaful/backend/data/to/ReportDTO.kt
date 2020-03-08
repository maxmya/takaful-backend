package com.takaful.backend.data.to

import java.sql.Timestamp

data class ReportDTO  (
        val id: Int,
val payload: String,
val timestamp: Timestamp
)