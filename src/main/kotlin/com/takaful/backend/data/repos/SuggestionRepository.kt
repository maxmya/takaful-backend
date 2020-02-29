package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.Suggestion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SuggestionRepository : JpaRepository<Suggestion, Int>