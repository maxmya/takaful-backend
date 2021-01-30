package com.takaful.backend.service.implementation

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.takaful.backend.data.entites.Medication
import com.takaful.backend.data.entites.Preservation
import com.takaful.backend.data.repos.CategoryRepository
import com.takaful.backend.data.repos.MedicationRepository
import com.takaful.backend.data.repos.PreservationRepository
import com.takaful.backend.data.repos.UserRepository
import com.takaful.backend.data.to.*
import com.takaful.backend.exceptions.ServiceException
import com.takaful.backend.security.JwtProvider
import com.takaful.backend.service.freamwork.*
import com.takaful.backend.utils.Pageable
import com.takaful.backend.utils.service.freamwork.PaginationCalcService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.sql.Timestamp
import java.util.*
import kotlin.math.*


@Service
class MedicationsServiceImpl @Autowired constructor(
    val medicationRepository: MedicationRepository,
    val userRepository: UserRepository,
    val preservationRepository: PreservationRepository,
    val jwtProvider: JwtProvider,
    val firebaseMessagingService: FirebaseMessagingService,
    val categoryRepository: CategoryRepository,
    val filesStorageService: FilesStorageService,
    val pagination: PaginationCalcService
) : MedicationsService {


    override fun getAllMedications(
        page: Int,
        latitude: String, longitude: String,
        size: Int, query: String, categoryId: Int
    ): Pageable<MedicationsDTO> {
        return try {

            var medications = medicationRepository.findAllByOrderByTimestampDesc()
            if (query != "" || categoryId != 0) {
                medications = searchMedications(medications, query, categoryId);
            }
            var listOfMedicationsDTOs = mutableListOf<MedicationsDTO>()

            for (medicine in medications) {
                if (medicine.preservation != null) continue
                listOfMedicationsDTOs.add(convertMedicationEntityToDTO(medicine))
            }


            pagination.getListAfterPaging(
                sortLocations(listOfMedicationsDTOs, latitude.toDouble(), longitude.toDouble()), page, size
            ) as Pageable<MedicationsDTO>

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ServiceException("cannot get all medications")
        }
    }


    fun sortLocations(
        locations: MutableList<MedicationsDTO>,
        myLatitude: Double,
        myLongitude: Double
    ): MutableList<MedicationsDTO> {
        val medicationsDistance = mutableMapOf<Double, MedicationsDTO>()
        locations.forEach { medication ->
            medicationsDistance[distance(myLatitude, myLongitude, medication.lat, medication.lang)] = medication
        }


        var sortedDistances = medicationsDistance.keys.sorted()


        val sortedMedications = mutableListOf<MedicationsDTO>()

        sortedDistances.forEach { key ->

            val med = medicationsDistance[key]
            if (med != null)
                sortedMedications.add(med)
        }


        return sortedMedications
    }


    fun distance(fromLat: Double, fromLon: Double, toLat: Double, toLon: Double): Double {
        val radius = 6378137.0 // approximate Earth radius, *in meters*
        val deltaLat = toLat - fromLat
        val deltaLon = toLon - fromLon
        val angle = 2 * asin(
            sqrt(
                sin(deltaLat / 2).pow(2.0) +
                        cos(fromLat) * cos(toLat) *
                        sin(deltaLon / 2).pow(2.0)
            )
        )
        return radius * angle
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


    private fun searchMedications(medications: List<Medication>, query: String, categoryId: Int): List<Medication> {
        println("inside search")
        return medications.filter {
            (if (query == "") {
                false
            } else {
                it.name.toLowerCase().contains(query, true)
            }) || (it.category?.id == categoryId)
        }

    }

    override fun convertMedicationEntityToDTO(medicine: Medication): MedicationsDTO {

        var user: MedicineUserDTO? = null
        var category: MedicineCategoryDTO? = null
        var preserver: PreservationsDTO? = null

        if (medicine.preservation != null) {
            preserver = PreservationsDTO(
                medicine.preservation!!.id,
                medicine.preservation!!.timestamp
            )
        }

        category = MedicineCategoryDTO(
            medicine.category.id,
            medicine.category.name,
            medicine.category.imageUrl
        )

        user = MedicineUserDTO(
            medicine.user.id,
            medicine.user.username,
            medicine.user.phone,
            medicine.user.fullName,
            medicine.user.pictureUrl
        )

        return MedicationsDTO(
            medicine.id,
            medicine.name,
            medicine.lang,
            medicine.lat,
            medicine.imageUrl,
            medicine.addressTitle,
            user, category, preserver
        )
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

            val medicine = Medication(
                name = medicationCreationForm.name,
                lang = medicationCreationForm.lang,
                lat = medicationCreationForm.lat,
                addressTitle = medicationCreationForm.address,
                user = postingUser,
                category = medicationCategory,
                imageUrl = fileDownloadUri,
                timestamp = Timestamp(Date().time),
                preservation = null
            )

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
                        if (medicine.preservation != null) {
                            return ResponseWrapper(false, "alreadyPreserved", null)
                        }
                        val preserver = Preservation(
                            timestamp = Timestamp(System.currentTimeMillis()),
                            medication = medicine, user = user
                        )
                        preservationRepository.save(preserver)
                        medicine.preservation = preserver
                        medicationRepository.save(medicine)
                        firebaseMessagingService
                            .sendNotification(
                                medicine.user.id.toString(),
                                PRESERVATION_TITLE,
                                "تم حجز دوائك بعنوان ${medicine.name} بنجاح ",
                                PRESERVATION_CHANNEL
                            )
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

    override fun getMyPostedMedications(token: String): ResponseWrapper {
        return try {
            val username = jwtProvider.getUserNameFromJwtToken(token)
            val user = userRepository.findUserByUsername(username)
            val myMedicationList = medicationRepository.findAllByUserOrderByTimestampDesc(user)
            val myMedicationListDto = mutableListOf<MedicationsDTO>()
            myMedicationList.forEach { medication ->
                myMedicationListDto.add(convertMedicationEntityToDTO(medication))
            }
            ResponseWrapper(true, "done", myMedicationListDto)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseWrapper(false, e.message.toString(), null)
        }
    }

    override fun getMyMedicationPreserverInfo(medicationId: Int): ResponseWrapper {
        return try {
            val selectedMedication = medicationRepository.getOne(medicationId)
            val preserverUser = selectedMedication.preservation?.user
            if (preserverUser != null) {
                val sentUser = MedicineUserDTO(
                    preserverUser.id,
                    preserverUser.username,
                    preserverUser.phone,
                    preserverUser.fullName,
                    preserverUser.pictureUrl
                )
                ResponseWrapper(true, "done", sentUser)
            } else throw ServiceException("preserver info not found")
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseWrapper(false, e.message.toString(), null)
        }
    }

    override fun deleteMyPostedMedication(token: String, medicationId: Int): ResponseWrapper {
        return try {
            val username = jwtProvider.getUserNameFromJwtToken(token)
            val medication = medicationRepository.getOne(medicationId)
            if (medication.user.username != username) {
                throw ServiceException("you are not the owner of this medication !")
            }
            medicationRepository.deleteById(medicationId)
            ResponseWrapper(true, "done", null)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseWrapper(false, e.message.toString(), null)
        }
    }


    override fun listUserPreservation(token: String): ResponseWrapper {
        return try {
            val username = jwtProvider.getUserNameFromJwtToken(token)
            val medications = medicationRepository.findAll()
            val listOfMedications = mutableListOf<Medication>()
            val listOfPreservation = mutableListOf<UserPreservationDTO>()
            println("username: $username")

            for (medicine in medications) {
                println(" preservation username: " + medicine.preservation?.user?.username)

                if (medicine.preservation?.user?.username == username) {
                    listOfMedications.add(medicine)
                }
            }
            for (medicine in listOfMedications) {
                val userPreservation = UserPreservationDTO(
                    medicine.preservation?.id,
                    convertMedicationEntityToDTO(medicine),
                    medicine.preservation?.timestamp
                )
                listOfPreservation.add(userPreservation)
            }
            ResponseWrapper(true, "done", listOfPreservation)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ResponseWrapper(false, "cannot get those", null)
        }
    }

    override fun deletePreservation(token: String, preservationId: Int): ResponseWrapper {
        return try {
            val medications = medicationRepository.findAll()
            for (medicine in medications) {
                if (medicine.preservation != null) {
                    if (medicine.preservation?.id == preservationId) {
                        medicine.preservation = null
                        medicationRepository.save(medicine)
                        break
                    }
                }
            }
            preservationRepository.deleteById(preservationId)
            return ResponseWrapper(true, "Preservation  deleted successfully", null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ResponseWrapper(false, ex.message.toString(), null)
        }
    }
}

