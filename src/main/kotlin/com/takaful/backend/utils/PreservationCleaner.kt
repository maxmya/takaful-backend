package com.takaful.backend.utils

import com.takaful.backend.data.repos.MedicationRepository
import com.takaful.backend.data.repos.PreservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log


@Service
class PreservationCleaner @Autowired constructor(val medicationRepository: MedicationRepository,
                                                 val preservationRepository: PreservationRepository) {


    @Scheduled(cron = "0 0 0/3 * * ?")
    private fun cleanOldReservations() {
        val medications = medicationRepository.findAll()
        val currentTime = Timestamp(Date().time)
        medications.forEach { medication ->
            if (medication.preservation != null) {
                val presTime = medication.preservation!!.timestamp
                val differenceInHours = getTimeDiff(presTime, currentTime, TimeUnit.HOURS)
                if (differenceInHours >= 3) {
                    print("CLEAR PRESERVED MED")
                    medication.preservation = null
                    medicationRepository.save(medication)
                }
            }
        }
    }

    fun getTimeDiff(oldTs: Timestamp, newTs: Timestamp, timeUnit: TimeUnit): Long {
        val diffInMS = newTs.time - oldTs.time
        return timeUnit.convert(diffInMS, TimeUnit.MILLISECONDS)
    }


}