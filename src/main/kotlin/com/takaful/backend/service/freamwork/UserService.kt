package com.takaful.backend.service.freamwork

import com.takaful.backend.controllers.*
import com.takaful.backend.data.to.ResponseWrapper
import org.springframework.web.multipart.MultipartFile

interface UserService {
    fun registerUser(userRegisterRequest: UserRegisterRequest): ResponseWrapper
    fun authenticateUser(userTokenRequest: UserTokenRequest): ResponseWrapper
    fun getUserProfile(userTokenRequest: UserTokenRequest): ResponseWrapper
    fun changeUserProfile(changeProfile: ChangeProfileRequest, file: MultipartFile): ResponseWrapper
}

