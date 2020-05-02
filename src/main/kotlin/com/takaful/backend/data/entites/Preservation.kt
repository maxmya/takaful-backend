package com.takaful.backend.data.entites

import org.hibernate.annotations.DynamicUpdate
import java.sql.Timestamp
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "preservation_table", schema = "dawa_db", catalog = "dawa_db")
data class Preservation(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,
        val timestamp: Timestamp,
        @OneToOne
        @JoinColumn(name = "medication_id", referencedColumnName = "id", nullable = true)
        val medication: Medication,
        @ManyToOne
        @JoinColumn(name = "preserver_id", referencedColumnName = "id", nullable = false)
        val user: User
)