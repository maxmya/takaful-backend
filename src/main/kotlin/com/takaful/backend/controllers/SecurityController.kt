package com.takaful.backend.controllers

import com.takaful.backend.data.to.*
import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.service.freamwork.UserService
import com.takaful.backend.utils.HeadersParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/user")
class SecurityController @Autowired constructor(
    val userService: UserService,
    val medicationsService: MedicationsService,
    val headersParser: HeadersParser
) {


    @GetMapping("/auth/registered/{phone}")
    fun isUserRegistered(@PathVariable("phone") phone: String): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(userService.isUserRegistered(phone))
    }


    @PostMapping("/auth/changePassword")
    fun changePassword(@RequestBody changePasswordRequest: ChangePasswordRequest): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(
            userService.changePassword(
                changePasswordRequest.phone,
                changePasswordRequest.password
            )
        )
    }


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
        @RequestPart(name = "body") changeProfileRequest: ChangeProfileRequest
    ): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(userService.changeUserProfile(changeProfileRequest, file))
    }

    @GetMapping("/auth/preservation")
    fun listUserPreservations(@RequestHeader(value = "Authorization") headers: HttpHeaders): ResponseEntity<ResponseWrapper> {
        val auth = headers.getFirst("Authorization")
        val token = headersParser.parseToken(auth)
        return ResponseEntity.ok(medicationsService.listUserPreservation(token))
    }

    @DeleteMapping("/auth/preservation/{id}")
    fun deletePreservation(
        @RequestHeader(value = "Authorization") headers: HttpHeaders,
        @PathVariable(value = "id") id: Int
    ): ResponseEntity<ResponseWrapper> {
        val auth = headers.getFirst("Authorization")
        val token = headersParser.parseToken(auth)
        return ResponseEntity.ok(medicationsService.deletePreservation(token, id))
    }

}

data class ChangePasswordRequest(
    val phone: String,
    val password: String
)

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
