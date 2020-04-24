package com.takaful.backend.service.implementation

import com.takaful.backend.controllers.ChangeProfileRequest
import com.takaful.backend.controllers.UserRegisterRequest
import com.takaful.backend.controllers.UserTokenRequest
import com.takaful.backend.data.entites.User
import com.takaful.backend.data.repos.UserRepository
import com.takaful.backend.data.to.ResponseWrapper
import com.takaful.backend.data.to.UserProfileResponse
import com.takaful.backend.security.JwtProvider
import com.takaful.backend.service.freamwork.FilesStorageService
import com.takaful.backend.service.freamwork.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder


@Service
class UserServiceImpl @Autowired constructor(val userRepository: UserRepository,
                                             val passwordEncoder: PasswordEncoder,
                                             val authenticationManager: AuthenticationManager,
                                             val jwtProvider: JwtProvider,
                                             val filesStorageService: FilesStorageService) : UserService {


    override fun registerUser(userRegisterRequest: UserRegisterRequest)
            : ResponseWrapper {

        if (userRepository.existsByUsername(userRegisterRequest.username)) {
            return ResponseWrapper(false,
                    "user ${userRegisterRequest.username} is already taken"
                    , null)
        }

        return try {

            val user = User(username = userRegisterRequest.username,
                    password = passwordEncoder.encode(userRegisterRequest.password),
                    phone = userRegisterRequest.phone,
                    fullName = userRegisterRequest.fullName,
                    pictureUrl = userRegisterRequest.pictureUrl)

            userRepository.save(user)

            ResponseWrapper(true, "user ${user.username} registered successful", null)
        } catch (e: Exception) {
            ResponseWrapper(false, "an error has occurred" + e.message, null)
        }
    }


    // notice that if return comes after control statements
    // like (try-if-when-switch) the last line object will be returned
    override fun authenticateUser(userTokenRequest: UserTokenRequest): ResponseWrapper {
        return try {
            val authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userTokenRequest.username,
                            userTokenRequest.password))
            SecurityContextHolder.getContext().authentication = authentication
            ResponseWrapper(true, "", jwtProvider.generateJwtToken(authentication))
        } catch (ex: Exception) {
            ResponseWrapper(false, "error", null)
        }
    }


    override fun getUserProfile(userTokenRequest: UserTokenRequest): ResponseWrapper {
        return try {
            val authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userTokenRequest.username,
                            userTokenRequest.password))
            SecurityContextHolder.getContext().authentication = authentication
            val token = jwtProvider.generateJwtToken(authentication)

            val userData = userRepository.findUserByUsername(userTokenRequest.username)

            val profile = UserProfileResponse(
                    userData.id,
                    userData.phone,
                    userData.fullName,
                    userData.pictureUrl,
                    token)

            ResponseWrapper(true, "user loaded", profile)
        } catch (ex: Exception) {
            if (ex.message == null) {
                ResponseWrapper(false, "cannot get profile for user", null)
            } else {
                ResponseWrapper(false, ex.message!!, null)
            }
        }
    }

    override fun changeUserProfile(changeProfile: ChangeProfileRequest, file: MultipartFile): ResponseWrapper {

        return try {

            if (changeProfile.oldUsername.isEmpty().or(changeProfile.oldUsername.isBlank())) {
                return ResponseWrapper(false, "invalid profile data", null)
            }

            if ((changeProfile.oldUsername == changeProfile.newUsername).not()) {
                return ResponseWrapper(false, "changing username is not supported now", null)
            }

            val userData = userRepository.findUserByUsername(changeProfile.oldUsername)
            val imgUrl = filesStorageService.storeFile(file)

            val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/storage/downloadFile/")
                    .path(imgUrl)
                    .toUriString()

            val user = User(
                    id = userData.id,
                    username = changeProfile.oldUsername,
                    password = userData.password,
                    phone = userData.phone,
                    fullName = changeProfile.fullName,
                    pictureUrl = fileDownloadUri)

            userRepository.save(user)

            ResponseWrapper(true, "User profile changed Successfully", user)
            
        } catch (ex: Exception) {
            ex.printStackTrace()
            ResponseWrapper(false, "error", null)
        }
    }
}