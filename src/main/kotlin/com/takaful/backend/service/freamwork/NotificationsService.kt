package com.takaful.backend.service.freamwork

import com.takaful.backend.data.to.NotificationDTO
import com.takaful.backend.data.to.ResponseWrapper

interface NotificationsService {

    fun getAllNotificationsForUser(userid: Int): ResponseWrapper
    fun deleteNotification(notificationId: Int): ResponseWrapper
}