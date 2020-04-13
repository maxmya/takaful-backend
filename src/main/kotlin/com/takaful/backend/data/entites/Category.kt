package com.takaful.backend.data.entites

import javax.persistence.*

@Entity
@Table(name = "categories_table", schema = "dawa_db", catalog = "dawa_db")
data class Category(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val name: String,
        @Column(name = "image_url")
        val imageUrl: String,
        @OneToMany(mappedBy = "category")
        val medications: List<Medication>
)