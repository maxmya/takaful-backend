package com.takaful.backend.service.freamwork

import com.takaful.backend.controllers.TokenResponse
import com.takaful.backend.controllers.UserTokenRequest
import com.takaful.backend.controllers.UserRegisterRequest
import com.takaful.backend.controllers.UserRegisterResponse

interface UserService {
    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse
    fun authenticateUser(userTokenRequest: UserTokenRequest): TokenResponse
}

