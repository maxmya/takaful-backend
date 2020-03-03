package com.takaful.backend.data.to

import java.sql.Timestamp

data class Report  (
        val id: Int,
val payload: String,
val timestamp: Timestamp
)