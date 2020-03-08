package com.takaful.backend.service.freamwork

import com.takaful.backend.data.entites.Medication
import com.takaful.backend.data.to.MedicationsDTO
import com.takaful.backend.utils.Pageable

interface MedicationsService {
    fun getAllMedications(page: Int, size: Int,query:String): Pageable<*>
    fun convertMedicationEntityToDTO(medicine: Medication): MedicationsDTO
}