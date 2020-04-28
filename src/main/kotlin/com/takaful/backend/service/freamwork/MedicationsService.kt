package com.takaful.backend.service.freamwork

import com.takaful.backend.data.entites.Medication
import com.takaful.backend.data.to.MedicationsDTO
import com.takaful.backend.data.to.ResponseWrapper
import com.takaful.backend.utils.Pageable

interface MedicationsService {
    fun getAllMedications(page: Int, size: Int,query:String): Pageable<MedicationsDTO>
    fun convertMedicationEntityToDTO(medicine: Medication): MedicationsDTO
    fun getMedicationsDetails(id: Int): MedicationsDTO
    fun medicinePreservation(token: String, id: Int): ResponseWrapper


}