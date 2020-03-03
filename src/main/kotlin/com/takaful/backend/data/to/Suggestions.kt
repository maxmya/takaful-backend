package com.takaful.backend.data.to

import java.sql.Timestamp

data class Suggestions (val id: Int,
                   val type: String,
                   val timestamp: Timestamp,
                   val title: String,
                   val body: String )