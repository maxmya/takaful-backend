package com.takaful.backend.service.implementation

import com.takaful.backend.data.repos.CategoryRepository
import com.takaful.backend.data.to.MedicineCategoryDTO
import com.takaful.backend.service.freamwork.CategoriesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoriesServiceImpl @Autowired constructor(private val categoryRepository: CategoryRepository) : CategoriesService {


    override fun listAllCategories(): List<MedicineCategoryDTO> {
        val listOfCategories = categoryRepository.findAll()
        val listOfCategoryDTO = mutableListOf<MedicineCategoryDTO>()
        listOfCategories.forEach { cat ->
            listOfCategoryDTO.add(MedicineCategoryDTO(cat.id, cat.name, cat.imageUrl))
        }
        return listOfCategoryDTO
    }


}