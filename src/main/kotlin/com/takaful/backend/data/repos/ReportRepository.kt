package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<Report, Int>