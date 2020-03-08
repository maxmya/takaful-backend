package com.takaful.backend.service.implementation

import com.takaful.backend.data.entites.Medication
import com.takaful.backend.data.repos.MedicationRepository
import com.takaful.backend.data.to.MedicationsDTO
import com.takaful.backend.data.to.MedicineCategoryDTO
import com.takaful.backend.data.to.MedicineUserDTO
import com.takaful.backend.data.to.PreservationsDTO
import com.takaful.backend.exceptions.ServiceException
import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.utils.service.freamwork.PaginationCalcService
import com.takaful.backend.utils.Pageable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MedicationsServiceImpl @Autowired constructor(val medicationRepository: MedicationRepository,
                                                    val pagination: PaginationCalcService) : MedicationsService {


    override fun getAllMedications(page: Int, size: Int): Pageable<*> {
        return try {
            val medications = medicationRepository.findAll()

            val listOfMedicationsDTOs = mutableListOf<MedicationsDTO>()

            for (medicine in medications) {
                listOfMedicationsDTOs.add(convertMedicationEntityToDTO(medicine))
            }
            pagination.getListAfterPaging(listOfMedicationsDTOs, page, size)
        } catch (ex: Exception) {
            throw ServiceException("cannot get all medications")
        }
    }

    override fun convertMedicationEntityToDTO(medicine: Medication): MedicationsDTO {

        val user = MedicineUserDTO(
                medicine.user.id,
                medicine.user.username,
                medicine.user.phone,
                medicine.user.fullName,
                medicine.user.pictureUrl)

        val category = MedicineCategoryDTO(
                medicine.category.id,
                medicine.category.name,
                medicine.category.imageUrl)

        val preserver = PreservationsDTO(
                medicine.preservation.id,
                medicine.preservation.timestamp)

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

