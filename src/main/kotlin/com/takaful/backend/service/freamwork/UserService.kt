package com.takaful.backend.service.freamwork

import com.takaful.backend.controllers.UserRegisterRequest
import com.takaful.backend.controllers.UserRegisterResponse

interface UserService {
    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse
}

