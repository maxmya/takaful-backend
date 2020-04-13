package com.takaful.backend.data.entites

import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "reports_table", schema = "dawa_db", catalog = "dawa_db")
data class Report(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val payload: String,
        val timestamp: Timestamp,
        @ManyToOne
        @JoinColumn(name = "reported_user", referencedColumnName = "id", nullable = false)
        val reportedUser: User,
        @ManyToOne
        @JoinColumn(name = "reporter_id", referencedColumnName = "id", nullable = false)
        val reporterUser: User,
        @ManyToOne
        @JoinColumn(name = "reported_medication", referencedColumnName = "id", nullable = true)
        val reportedMedication: Medication
)