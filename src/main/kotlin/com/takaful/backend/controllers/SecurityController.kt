package com.takaful.backend.controllers

import com.takaful.backend.data.entites.*
import com.takaful.backend.service.freamwork.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/user")
class SecurityController @Autowired constructor(val userService: UserService) {

    @PostMapping("/auth/register")
    fun registerUser(@RequestBody userRegisterRequest: UserRegisterRequest): ResponseEntity<UserRegisterResponse> {
        return ResponseEntity.ok(userService.registerUser(userRegisterRequest))
    }

    @PostMapping("/auth/token")
    fun authenticateUser(@RequestBody tokenRequest: UserTokenRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(userService.authenticateUser(tokenRequest))
    }

    @PostMapping("/auth/login")
    fun getUser(@RequestBody tokenRequest: UserTokenRequest): ResponseEntity<UserProfileResponse> {
        return ResponseEntity.ok(userService.getUserProfile(tokenRequest))
    }

}

// notice Marwa that its ok to write data objects that is used only once in the using file (not class)
// and yes its imported into the controller but this cycle is called use-case
// if an object is shared to another use-case we put it into a general package
// in a separating file
data class UserRegisterRequest(
        val username: String,
        val password: String,
        val phone: String,
        val fullName: String,
        val pictureUrl: String
)

data class UserRegisterResponse(
        val success: Boolean,
        val message: String
)

data class UserTokenRequest(
        val username: String,
        val password: String
)

data class TokenResponse(
        val success: Boolean,
        val jwtToken: String
)
data class UserProfileResponse(
        val phone: String,
        val fullName: String,
        val pictureUrl: String,
        val medications: List<Medication>,
        val reports: List<Report>,
        val preservations: List<Preservation>,
        val suggestions: List<Suggestion>,
        val notifications: List<Notification>
)