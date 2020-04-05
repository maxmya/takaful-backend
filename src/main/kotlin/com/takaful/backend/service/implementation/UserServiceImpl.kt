package com.takaful.backend.service.implementation

import com.takaful.backend.controllers.*
import com.takaful.backend.data.entites.User
import com.takaful.backend.data.repos.UserRepository
import com.takaful.backend.data.to.ConfirmationClass
import com.takaful.backend.data.to.UserProfileResponse
import com.takaful.backend.security.JwtProvider
import com.takaful.backend.service.freamwork.MedicationsService
import com.takaful.backend.service.freamwork.UserService
import org.hibernate.service.spi.ServiceException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sun.security.krb5.Confounder.bytes
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class UserServiceImpl @Autowired constructor(val userRepository: UserRepository,
                                             val medicationsService: MedicationsService,
                                             val passwordEncoder: PasswordEncoder,
                                             val authenticationManager: AuthenticationManager,
                                             val jwtProvider: JwtProvider,
                                             val filesStorageServiceImpl: FilesStorageServiceImpl) : UserService {

    override fun registerUser(userRegisterRequest: UserRegisterRequest)
            : UserRegisterResponse {

        if (userRepository.existsByUsername(userRegisterRequest.username)) {
            return UserRegisterResponse(false, "user ${userRegisterRequest.username} is already taken")
        }

        return try {

            val user = User(username = userRegisterRequest.username,
                    password = passwordEncoder.encode(userRegisterRequest.password),
                    phone = userRegisterRequest.phone,
                    fullName = userRegisterRequest.fullName,
                    pictureUrl = userRegisterRequest.pictureUrl)

            userRepository.save(user)

            UserRegisterResponse(true, "user ${user.username} registered successful")
        } catch (e: Exception) {
            UserRegisterResponse(false, "an error has occurred" + e.message)
        }
    }


    // notice that if return comes after control statements
    // like (try-if-when-switch) the last line object will be returned
    override fun authenticateUser(userTokenRequest: UserTokenRequest): TokenResponse {
        return try {
            val authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userTokenRequest.username,
                            userTokenRequest.password))
            SecurityContextHolder.getContext().authentication = authentication
            TokenResponse(true, jwtProvider.generateJwtToken(authentication))
        } catch (ex: Exception) {
            TokenResponse(false, "error")
        }
    }


    override fun getUserProfile(userTokenRequest: UserTokenRequest): UserProfileResponse {

        return try {

            val authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userTokenRequest.username,
                            userTokenRequest.password))
            SecurityContextHolder.getContext().authentication = authentication
            val token = jwtProvider.generateJwtToken(authentication)

            val userData = userRepository.findUserByUsername(userTokenRequest.username)

            UserProfileResponse(
                    userData.id,
                    userData.phone,
                    userData.fullName,
                    userData.pictureUrl,
                    token)

        } catch (ex: Exception) {
            throw ServiceException("cannot get user profile")
        }
    }

    override fun changeUserProfile(token:String,changeProfile: ChangeProfileRequest,file: MultipartFile): ConfirmationClass {

        return try {
            val username = jwtProvider.getUserNameFromJwtToken(token)
            if(changeProfile.fullName=="" || changeProfile.phone=="" || changeProfile.userName==""){
                return ConfirmationClass(false,"user data  is Empty")
            }
            if(username == ""){
                return ConfirmationClass(false,"UserName is Empty")
            }
            if (userRepository.existsByUsername(changeProfile.userName)) {
                return ConfirmationClass(false, "user ${changeProfile.userName} is already taken")
            }else {
                val userData = userRepository.findUserByUsername(username)
                var imgUrl: String=changeProfile.pictureUrl;
                if(!file.isEmpty){
                    imgUrl= filesStorageServiceImpl.save(file)
                }
                val user = User(
                        id = userData.id,
                        username = changeProfile.userName,
                        password = userData.password,
                        phone = changeProfile.phone,
                        fullName = changeProfile.fullName,
                        pictureUrl = imgUrl)
                userRepository.save(user)

                ConfirmationClass(true, "User profile changed Successfully")

            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            ConfirmationClass(false,"error")
        }
    }
}