package com.takaful.backend.service.implementation

import com.takaful.backend.controllers.UserRegisterRequest
import com.takaful.backend.controllers.UserRegisterResponse
import com.takaful.backend.service.freamwork.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    override fun registerUser(userRegisterRequest: UserRegisterRequest)
            : UserRegisterResponse {
        return UserRegisterResponse(false, "testing-message");
    }

}