package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Int>