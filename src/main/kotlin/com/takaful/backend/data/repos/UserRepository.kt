package com.takaful.backend.data.repos

import com.takaful.backend.data.entites.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int>