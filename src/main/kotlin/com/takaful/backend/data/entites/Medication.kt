package com.takaful.backend.data.entites

import javax.persistence.*


@Entity
@Table(name = "medication_table", schema = "dawa_db", catalog = "dawa_db")
data class Medication(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,
        val name: String,
        val lang: Double,
        val lat: Double,
        @Column(name = "image_url")
        val imageUrl: String,
        @Column(name = "address_title")
        val addressTitle: String,
        @ManyToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)//must not be null as medicine must be uploaded by user
        val user: User?,
        @ManyToOne
        @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = true)
        val category: Category?,
        @OneToOne
        @JoinColumn(name = "preservation_id", referencedColumnName = "id", nullable = true)
        val preservation: Preservation?
)