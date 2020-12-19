package com.takaful.backend.service.implementation

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.takaful.backend.data.entites.Notification
import com.takaful.backend.data.repos.NotificationRepository
import com.takaful.backend.data.repos.UserRepository
import com.takaful.backend.service.freamwork.FirebaseMessagingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class FirebaseMessagingServiceImpl @Autowired constructor(val firebaseApp: FirebaseApp,
                                                          val userRepository: UserRepository,
                                                          val notificationRepository: NotificationRepository) : FirebaseMessagingService {

    override fun sendNotification(userId: String, title: String, message: String, chanel: String) {

        val toSendMessage: Message = Message.builder()
                .putData("message", message)
                .putData("title", title)
                .putData("chanel", chanel)
                .setTopic(userId)
                .build()

        val response = FirebaseMessaging.getInstance(firebaseApp).send(toSendMessage)

        val user = userRepository.getOne(userId.toInt())

        val notification = Notification(
                title = title,
                body = message,
                timestamp = Timestamp(Date().time),
                notifiedUser = user,
                preservation = null)

        notificationRepository.save(notification)

        println("Successfully sent message: $response")

    }
}