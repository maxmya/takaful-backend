package com.takaful.backend.service.freamwork

import com.takaful.backend.data.entites.Medication
import com.takaful.backend.data.to.MedicationCreationForm
import com.takaful.backend.data.to.MedicationsDTO
import com.takaful.backend.data.to.ResponseWrapper
import com.takaful.backend.utils.Pageable
import org.springframework.web.multipart.MultipartFile

interface MedicationsService {

    fun getAllMedications(
        page: Int,
        latitude: String, longitude: String,
        size: Int, query: String, categoryId: Int
    ): Pageable<MedicationsDTO>

    fun convertMedicationEntityToDTO(medicine: Medication): MedicationsDTO
    fun getMedicationsDetails(id: Int): MedicationsDTO
    fun postMedication(medicationCreationForm: MedicationCreationForm, file: MultipartFile): ResponseWrapper
    fun medicinePreservation(token: String, id: Int): ResponseWrapper
    fun listUserPreservation(token: String): ResponseWrapper
    fun deletePreservation(token: String, preservationId: Int): ResponseWrapper
    fun getMyPostedMedications(token: String): ResponseWrapper
    fun getMyMedicationPreserverInfo(medicationId: Int): ResponseWrapper
    fun deleteMyPostedMedication(token: String, medicationId: Int): ResponseWrapper
}
