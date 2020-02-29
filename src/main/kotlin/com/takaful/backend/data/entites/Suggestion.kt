package com.takaful.backend.data.entites

import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "suggestion_table", schema = "public", catalog = "takaful-db")
data class Suggestion(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val type: String,
        val timestamp: Timestamp,
        val title: String,
        val body: String,
        @ManyToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
        val user: User
)