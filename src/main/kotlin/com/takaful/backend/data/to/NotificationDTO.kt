package com.takaful.backend.data.to

import java.sql.Timestamp

data class NotificationDTO(val id: Int,
                           val title: String,
                           val body: String,
                           val timestamp: Timestamp)