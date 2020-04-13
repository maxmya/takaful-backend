package com.takaful.backend.controllers

import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.utils.Pageable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/user")
class MedicationsController @Autowired constructor(val medicationsService: MedicationsService) {

    @GetMapping("/auth/medications")
    fun registerUser( @RequestParam(value = "q",  defaultValue = "",required = false)  query:String,
                      @RequestParam(value = "page", defaultValue = "1", required = false)  page:String,
                      @RequestParam(value = "size", defaultValue = "20", required = false)  size:String): ResponseEntity<Pageable<*>> {
        return ResponseEntity.ok(medicationsService.getAllMedications(page =page.toInt() ,size = size.toInt(),query = query))
    }
}
