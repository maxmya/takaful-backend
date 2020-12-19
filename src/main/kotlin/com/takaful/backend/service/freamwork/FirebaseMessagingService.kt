package com.takaful.backend.service.freamwork

val PRESERVATION_TITLE = "حجز الدواء"
val PRESERVATION_CHANNEL = "1"


interface FirebaseMessagingService {

    fun sendNotification(userId: String, title: String, message: String, chanel: String)

}