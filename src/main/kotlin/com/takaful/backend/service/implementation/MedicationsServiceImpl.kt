package com.takaful.backend.service.implementation

import com.takaful.backend.controllers.Medications
import com.takaful.backend.data.repos.MedicationRepository
import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.service.freamwork.PaginationCalcService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service

class MedicationsServiceImpl @Autowired constructor(val medicationRepository:MedicationRepository,val pagination: PaginationCalcService) : MedicationsService {


    override fun getAllMedications(page:Int,size:Int):  Pageable<*>{
        try {
            val medications=medicationRepository.findAll()
            val listOfMedications= mutableListOf<Medications>()
            for (medicine in medications) {
                val medication=Medications(medicine.id,medicine.name,medicine.lang,medicine.lat,medicine.imageUrl,medicine.addressTitle,medicine.user.id,medicine.user.phone,medicine.user.fullName,medicine.user.pictureUrl,medicine.category.id,medicine.category.name,medicine.category.imageUrl,medicine.preservation.id,medicine.preservation.timestamp)
                listOfMedications.add(medication)
            }
            return pagination.getListAfterPaging(listOfMedications,page,size)
        } catch (ex: Exception) {
            throw Exception(ex)
        }
    }


}

