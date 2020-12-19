package com.takaful.backend.service.implementation

import com.takaful.backend.data.repos.NotificationRepository
import com.takaful.backend.data.repos.UserRepository
import com.takaful.backend.data.to.NotificationDTO
import com.takaful.backend.data.to.ResponseWrapper
import com.takaful.backend.service.freamwork.NotificationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotificationsServiceImpl @Autowired constructor(val notificationRepository: NotificationRepository,
                                                      val userRepository: UserRepository) : NotificationsService {


    override fun getAllNotificationsForUser(userid: Int): ResponseWrapper {
        return try {
            val user = userRepository.getOne(userid)
            val notifications = notificationRepository.findAllByNotifiedUser(user)
            val notificationDTO = mutableListOf<NotificationDTO>()
            notifications.forEach { item ->
                notificationDTO.add(NotificationDTO(item.id
                        , item.title, item.body, item.timestamp))
            }
            ResponseWrapper(true, "done", notificationDTO)
        } catch (e: Exception) {
            ResponseWrapper(false, "error", null)
        }
    }

    override fun deleteNotification(notificationId: Int): ResponseWrapper {
        return try {
            notificationRepository.deleteById(notificationId)
            ResponseWrapper(true, "done", null)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseWrapper(false, "error", null)
        }
    }
}