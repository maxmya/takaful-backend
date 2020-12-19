package com.takaful.backend.controllers

import com.takaful.backend.data.to.*
import com.takaful.backend.service.freamwork.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/user")
class SecurityController @Autowired constructor(val userService: UserService) {

    @PostMapping("/auth/register")
    fun registerUser(@RequestBody userRegisterRequest: UserRegisterRequest): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(userService.registerUser(userRegisterRequest))
    }

    @PostMapping("/auth/token")
    fun authenticateUser(@RequestBody tokenRequest: UserTokenRequest): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(userService.authenticateUser(tokenRequest))
    }

    @PostMapping("/auth/login")
    fun getUser(@RequestBody tokenRequest: UserTokenRequest): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(userService.getUserProfile(tokenRequest))
    }

    @PutMapping("/auth/user")
    fun changeUserProfile(
            @RequestPart(name = "file") file: MultipartFile,
            @RequestPart(name = "body") changeProfileRequest: ChangeProfileRequest): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(userService.changeUserProfile(changeProfileRequest, file))
    }

}

data class UserRegisterRequest(
        val username: String,
        val password: String,
        val phone: String,
        val fullName: String,
        val pictureUrl: String
)


data class UserTokenRequest(
        val username: String,
        val password: String
)

data class TokenResponse(
        val jwtToken: String
)

data class ChangeProfileRequest(
        val fullName: String,
        val oldUsername: String,
        val newUsername: String
)
