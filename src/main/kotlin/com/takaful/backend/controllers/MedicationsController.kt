package com.takaful.backend.controllers

import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.service.implementation.Pageable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/user")
class MedicationsController @Autowired constructor(val medicationsService: MedicationsService) {

    @GetMapping("/auth/medications")
    fun registerUser( @RequestParam(value = "page", defaultValue = "1", required = false)  page:String,
                      @RequestParam(value = "size", defaultValue = "20", required = false)  size:String): ResponseEntity<Pageable<*>> {
        return ResponseEntity.ok(medicationsService.getAllMedications(page =page.toInt() ,size = size.toInt()))
    }
}
data class Medications(
        val id: Int,
        val name: String,
        val lang: Double,
        val lat: Double,
        val imageUrl: String,
        val addressTitle: String,
        val userId:Int,
        val phone: String,
        val fullName: String,
        val pictureUrl: String,
        val categoryId: Int,
        val categoryName: String,
        val categoryImageUrl: String,
        val preserverId: Int,
        val preservingTimeStamp: Timestamp
)