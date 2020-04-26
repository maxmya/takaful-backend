package com.takaful.backend.service.implementation

import com.takaful.backend.data.entites.Medication
import com.takaful.backend.data.repos.MedicationRepository
import com.takaful.backend.data.to.MedicationsDTO
import com.takaful.backend.data.to.MedicineCategoryDTO
import com.takaful.backend.data.to.MedicineUserDTO
import com.takaful.backend.data.to.PreservationsDTO
import com.takaful.backend.exceptions.ServiceException
import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.utils.Pageable
import com.takaful.backend.utils.service.freamwork.PaginationCalcService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

@Service
class MedicationsServiceImpl @Autowired constructor(val medicationRepository: MedicationRepository,
                                                    val pagination: PaginationCalcService) : MedicationsService {


    override fun getAllMedications(page: Int, size: Int,query:String): Pageable<MedicationsDTO> {
        return try {
            var medications = medicationRepository.findAll()
            if(query!="") {
                medications = searchMedications(medications, query);
            }
            val listOfMedicationsDTOs = mutableListOf<MedicationsDTO>()

            for (medicine in medications) {

                listOfMedicationsDTOs.add(convertMedicationEntityToDTO(medicine))
            }
            pagination.getListAfterPaging(listOfMedicationsDTOs, page, size) as Pageable<MedicationsDTO>
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ServiceException("cannot get all medications")
        }
    }
    private  fun searchMedications(medications: List<Medication>,query: String):List<Medication>{
        return medications.stream()
                .filter { medicine: Medication -> medicine.name.toLowerCase().contains(query.toLowerCase()) }
                .collect(Collectors.toCollection<Any, List<Medication>> { ArrayList() })

    }
    override fun convertMedicationEntityToDTO(medicine: Medication): MedicationsDTO {
        var user:MedicineUserDTO?=null
        var category : MedicineCategoryDTO?=null
        var preserver : PreservationsDTO?=null

        if(medicine.user!=null) {
            user = MedicineUserDTO(
                    medicine.user.id,
                    medicine.user.username,
                    medicine.user.phone,
                    medicine.user.fullName,
                    medicine.user.pictureUrl)
        }
        if(medicine.category!=null) {
            category = MedicineCategoryDTO(
                    medicine.category.id,
                    medicine.category.name,
                    medicine.category.imageUrl)
        }
        if(medicine.preservation!=null) {
             preserver = PreservationsDTO(
                    medicine.preservation.id,
                    medicine.preservation.timestamp)
        }
        return MedicationsDTO(
                medicine.id,
                medicine.name,
                medicine.lang,
                medicine.lat,
                medicine.imageUrl,
                medicine.addressTitle,
                user, category, preserver)
    }

}

