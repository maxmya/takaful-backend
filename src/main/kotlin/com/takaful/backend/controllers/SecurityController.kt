package com.takaful.backend.controllers

import com.takaful.backend.service.freamwork.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/user")
class SecurityController @Autowired constructor(val userService: UserService) {

    @PostMapping("/auth/register")
    fun addUser(@RequestBody userRegisterRequest: UserRegisterRequest): ResponseEntity<UserRegisterResponse> {
        return ResponseEntity.ok(userService.registerUser(userRegisterRequest))
    }


}

// notice Marwa that its ok to write data objects that is used only once in the using file (not class)
// and yes its imported into the controller but this cycle is called use-case
// if an object is shared to another use-case we put it into a general package
// in a separating file
data class UserRegisterRequest(
        val username: String,
        val password: String
)

data class UserRegisterResponse(
        val success: Boolean,
        val message: String
)