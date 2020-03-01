package com.takaful.backend.service.freamwork

import com.takaful.backend.controllers.*

interface UserService {
    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse
    fun authenticateUser(userTokenRequest: UserTokenRequest): TokenResponse
    fun getUserProfile(userTokenRequest: UserTokenRequest): UserProfileResponse

    }

