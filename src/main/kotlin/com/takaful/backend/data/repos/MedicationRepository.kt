package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.Medication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MedicationRepository : JpaRepository<Medication, Int>