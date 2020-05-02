package com.takaful.backend.data.entites

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*


@Entity
@DynamicUpdate
@Table(name = "user_table", schema = "dawa_db", catalog = "dawa_db")
data class User(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,
        val username: String,
        val password: String,
        val phone: String,
        @Column(name = "full_name")
        val fullName: String,
        @Column(name = "picture_url")
        val pictureUrl: String,
        @OneToMany(mappedBy = "user")
        val medications: List<Medication> = mutableListOf(),
        @OneToMany(mappedBy = "notifiedUser")
        val notifications: List<Notification> = mutableListOf(),
        @OneToMany(mappedBy = "user")
        val preservations: List<Preservation> = mutableListOf(),
        @OneToMany(mappedBy = "reporterUser")
        val reports: List<Report> = mutableListOf(),
        @OneToMany(mappedBy = "user")
        val suggestions: List<Suggestion> = mutableListOf()
)

// notice that I made an empty list and 0 id as a trivial data for holding the entity
// object to be created by kotlin , kotlin val cannot be null or not passed to the data class
// so we need when we init user not to send those values