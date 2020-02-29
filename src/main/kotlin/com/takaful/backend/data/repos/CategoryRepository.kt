package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Int>