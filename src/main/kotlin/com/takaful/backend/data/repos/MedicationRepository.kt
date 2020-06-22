package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.Medication
import com.takaful.backend.data.entites.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MedicationRepository : JpaRepository<Medication, Int> {

    fun findAllByOrderByTimestampDesc(): List<Medication>

    fun findAllByUserOrderByTimestampDesc(user: User): List<Medication>

}