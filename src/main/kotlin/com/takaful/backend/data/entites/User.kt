package com.takaful.backend.data.entites

import javax.persistence.*


@Entity
@Table(name = "user_table", schema = "public", catalog = "takaful-db")
data class User(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val username: String,
        val password: String,
        val phone: String,
        @Column(name = "full_name")
        val fullName: String,
        @Column(name = "picture_url")
        val pictureUrl: String,
        @OneToMany(mappedBy = "user")
        val medications: List<Medication>,
        @OneToMany(mappedBy = "notifiedUser")
        val notifications: List<Notification>,
        @OneToMany(mappedBy = "user")
        val preservations: List<Preservation>,
        @OneToMany(mappedBy = "reporterUser")
        val reports: List<Report>,
        @OneToMany(mappedBy = "user")
        val suggestions: List<Suggestion>
)