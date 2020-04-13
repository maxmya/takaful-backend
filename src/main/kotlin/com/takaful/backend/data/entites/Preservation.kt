package com.takaful.backend.data.entites

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "preservation_table", schema = "dawa_db", catalog = "dawa_db")
data class Preservation(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val timestamp: Timestamp,
        @OneToOne
        @JoinColumn(name = "medication_id", referencedColumnName = "id", nullable = true)
        val medication: Medication,
        @ManyToOne
        @JoinColumn(name = "preserver_id", referencedColumnName = "id", nullable = false)
        val user: User
)