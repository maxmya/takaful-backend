package com.takaful.backend.data.entites

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "notifications_table", schema = "public", catalog = "takaful-db")
data class Notification(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val title: String,
        val body: String,
        val timestamp: Timestamp,
        @ManyToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
        val notifiedUser: User,
        @ManyToOne
        @JoinColumn(name = "preservation_id", referencedColumnName = "id", nullable = true)
        val preservation: Preservation
)