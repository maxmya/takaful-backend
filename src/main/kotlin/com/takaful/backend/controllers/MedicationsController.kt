package com.takaful.backend.controllers

import com.takaful.backend.data.to.MedicationCreationForm
import com.takaful.backend.data.to.MedicationsDTO
import com.takaful.backend.data.to.ResponseWrapper
import com.takaful.backend.service.freamwork.CategoriesService
import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.utils.HeadersParser
import com.takaful.backend.utils.Pageable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/medication")
class MedicationsController @Autowired constructor(val medicationsService: MedicationsService,
                                                   val categoriesService: CategoriesService,
                                                   val headersParser: HeadersParser) {


    @GetMapping("/categories")
    fun listAllCategories() = categoriesService.listAllCategories()


    @GetMapping("/list")
    fun listMedications(@RequestParam(value = "q", defaultValue = "", required = false) query: String,
                        @RequestParam(value = "page", defaultValue = "1", required = false) page: String,
                        @RequestParam(value = "size", defaultValue = "20", required = false) size: String): ResponseEntity<Pageable<MedicationsDTO>> {
        return ResponseEntity.ok(medicationsService.getAllMedications(page = page.toInt(), size = size.toInt(), query = query))
    }

    @GetMapping("/list/{id}")
    fun listSpecificMedicationDetails(@PathVariable(value = "id") id: Int): ResponseEntity<MedicationsDTO> {
        return ResponseEntity.ok(medicationsService.getMedicationsDetails(id))
    }

    @PostMapping("/auth/medications/{id}")
    fun medicinePreservation(@RequestHeader(value = "Authorization") headers: HttpHeaders,
                             @PathVariable(value = "id") id: Int): ResponseEntity<ResponseWrapper> {
        val auth = headers.getFirst("Authorization")
        val token = headersParser.parseToken(auth)
        return ResponseEntity.ok(medicationsService.medicinePreservation(token, id))
    }


    @PostMapping("/add")
    fun addMedication(
            @RequestPart(name = "file") file: MultipartFile,
            @RequestPart(name = "body") medicationCreationForm: MedicationCreationForm): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(medicationsService.postMedication(medicationCreationForm, file))
    }
}
