package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.Preservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PreservationRepository : JpaRepository<Preservation, Int>