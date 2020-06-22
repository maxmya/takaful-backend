package com.takaful.backend.controllers

import com.takaful.backend.data.to.ResponseWrapper
import com.takaful.backend.service.freamwork.NotificationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/notifications")
class NotificationController @Autowired constructor(val notificationsService: NotificationsService) {


    @GetMapping("/list/{userId}")
    fun getAllByUserId(@PathVariable("userId") userId: Int): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(notificationsService.getAllNotificationsForUser(userId))
    }


    @DeleteMapping("/delete/{notificationId}")
    fun deleteNotification(@PathVariable("notificationId") notificationId: Int): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(notificationsService.deleteNotification(notificationId))
    }


}
