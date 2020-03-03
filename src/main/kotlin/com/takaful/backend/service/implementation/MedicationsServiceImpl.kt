package com.takaful.backend.service.implementation

import com.takaful.backend.data.repos.MedicationRepository
import com.takaful.backend.data.to.Medications
import com.takaful.backend.data.to.MedicineCategory
import com.takaful.backend.data.to.MedicineUser
import com.takaful.backend.data.to.Preservations
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
                val user= MedicineUser(medicine.user.id,medicine.user.username,medicine.user.phone,medicine.user.fullName,medicine.user.pictureUrl)
                val category= MedicineCategory(medicine.category.id,medicine.category.name,medicine.category.imageUrl)
                val preserver= Preservations(medicine.preservation.id,medicine.preservation.timestamp)
                listOfMedications.add( Medications(medicine.id,medicine.name,medicine.lang,medicine.lat,medicine.imageUrl,medicine.addressTitle,user,category,preserver))
            }
            return pagination.getListAfterPaging(listOfMedications,page,size)
        } catch (ex: Exception) {
            throw Exception(ex)
        }
    }


}

