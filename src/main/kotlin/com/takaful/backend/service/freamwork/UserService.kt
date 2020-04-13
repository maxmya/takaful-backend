package com.takaful.backend.service.freamwork

import com.takaful.backend.controllers.*
import com.takaful.backend.data.to.ConfirmationClass
import com.takaful.backend.data.to.UserProfileResponse
import org.springframework.web.multipart.MultipartFile

interface UserService {
    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse
    fun authenticateUser(userTokenRequest: UserTokenRequest): TokenResponse
    fun getUserProfile(userTokenRequest: UserTokenRequest): UserProfileResponse
    fun changeUserProfile(token:String,changeProfile: ChangeProfileRequest,file: MultipartFile): ConfirmationClass ;

    }

