package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.Notification
import com.takaful.backend.data.entites.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Int> {

    fun findAllByNotifiedUser(notifiedUser: User): List<Notification>
}