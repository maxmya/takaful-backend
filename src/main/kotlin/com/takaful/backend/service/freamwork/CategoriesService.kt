package com.takaful.backend.service.freamwork

import com.takaful.backend.data.to.MedicineCategoryDTO

interface CategoriesService {
    fun listAllCategories(): List<MedicineCategoryDTO>
}