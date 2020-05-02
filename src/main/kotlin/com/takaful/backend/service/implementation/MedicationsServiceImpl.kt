package com.takaful.backend.service.implementation

import com.takaful.backend.data.entites.Medication
import com.takaful.backend.data.entites.Preservation
import com.takaful.backend.data.repos.CategoryRepository
import com.takaful.backend.data.repos.MedicationRepository
import com.takaful.backend.data.repos.PreservationRepository
import com.takaful.backend.data.repos.UserRepository
import com.takaful.backend.data.to.*
import com.takaful.backend.exceptions.ServiceException
import com.takaful.backend.security.JwtProvider
import com.takaful.backend.service.freamwork.FilesStorageService
import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.utils.Pageable
import com.takaful.backend.utils.service.freamwork.PaginationCalcService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.sql.Timestamp
import java.util.*
import java.util.stream.Collectors

@Service
class MedicationsServiceImpl @Autowired constructor(val medicationRepository: MedicationRepository,
                                                    val userRepository: UserRepository,
                                                    val preservationRepository: PreservationRepository,
                                                    val jwtProvider: JwtProvider,
                                                    val categoryRepository: CategoryRepository,
                                                    val filesStorageService: FilesStorageService,
                                                    val pagination: PaginationCalcService) : MedicationsService {


    override fun getAllMedications(page: Int, size: Int, query: String): Pageable<MedicationsDTO> {
        return try {
            var medications = medicationRepository.findAll()
            if (query != "") {
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

    override fun getMedicationsDetails(id: Int): MedicationsDTO {
        return try {
            if (id != 0) {
                val medicine = medicationRepository.findByIdOrNull(id)
                if (medicine == null) {
                    throw ServiceException("invalid Id")
                } else {
                    convertMedicationEntityToDTO(medicine)
                }

            } else {
                throw ServiceException("invalid Id")

            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex
        }
    }


    private fun searchMedications(medications: List<Medication>, query: String): List<Medication> {
        return medications.stream()
                .filter { medicine: Medication -> medicine.name.toLowerCase().contains(query.toLowerCase()) }
                .collect(Collectors.toCollection<Any, List<Medication>> { ArrayList() })

    }

    override fun convertMedicationEntityToDTO(medicine: Medication): MedicationsDTO {
        var user: MedicineUserDTO? = null
        var category: MedicineCategoryDTO? = null
        var preserver: PreservationsDTO? = null

        if (medicine.user != null) {
            user = MedicineUserDTO(
                    medicine.user.id,
                    medicine.user.username,
                    medicine.user.phone,
                    medicine.user.fullName,
                    medicine.user.pictureUrl)
        }
        if (medicine.category != null) {
            category = MedicineCategoryDTO(
                    medicine.category.id,
                    medicine.category.name,
                    medicine.category.imageUrl)
        }
        if (medicine.preservation != null) {
            preserver = PreservationsDTO(
                    medicine.preservation!!.id,
                    medicine.preservation!!.timestamp)
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


    override fun postMedication(medicationCreationForm: MedicationCreationForm, file: MultipartFile): ResponseWrapper {
        return try {

            if (!userRepository.existsById(medicationCreationForm.userId)) {
                throw ServiceException("user with id ${medicationCreationForm.userId} not found")
            }

            if (!categoryRepository.existsById(medicationCreationForm.categoryId)) {
                throw ServiceException("invalid category")
            }

            val imgUrl = filesStorageService.storeFile(file)

            val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/storage/downloadFile/")
                    .path(imgUrl)
                    .toUriString()


            val postingUser = userRepository.getOne(medicationCreationForm.userId)
            val medicationCategory = categoryRepository.getOne(medicationCreationForm.categoryId)

            val medicine = Medication(name = medicationCreationForm.name,
                    lang = medicationCreationForm.lang,
                    lat = medicationCreationForm.lat,
                    addressTitle = medicationCreationForm.address,
                    user = postingUser,
                    category = medicationCategory,
                    imageUrl = fileDownloadUri,
                    preservation = null)

            medicationRepository.save(medicine)

            ResponseWrapper(true, "done", null)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseWrapper(false, "cannot add medication", null)
        }
    }

    override fun medicinePreservation(token: String, id: Int): ResponseWrapper {
        return try {
            if (id != 0) {
                val medicine = medicationRepository.findByIdOrNull(id)
                if (medicine == null) {
                    ResponseWrapper(false, "invalid medicine Id", null)
                } else {
                    val username = jwtProvider.getUserNameFromJwtToken(token)
                    if (username == "") {
                        return ResponseWrapper(false, "unAuthorized User", null)
                    } else {
                        val user = userRepository.findUserByUsername(username)
                        if(medicine.preservation!=null){
                            return ResponseWrapper(false, "alreadyPreserved", null)
                        }
                        val preserver = Preservation(timestamp = Timestamp(System.currentTimeMillis()),
                                medication = medicine, user = user)
                        preservationRepository.save(preserver)
                        medicine.preservation = preserver
                        medicationRepository.save(medicine)
                        return ResponseWrapper(true, "medicine preserved successfully", null)

                    }
                }

            } else {
                return ResponseWrapper(false, "invalid medicine Id", null)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            ResponseWrapper(false, ex.message.toString(), null)
        }


    }
}

