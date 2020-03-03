package com.takaful.backend.service.implementation

import com.takaful.backend.controllers.*
import com.takaful.backend.data.entites.User
import com.takaful.backend.data.repos.UserRepository
import com.takaful.backend.data.to.*
import com.takaful.backend.security.JwtProvider
import com.takaful.backend.service.freamwork.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.math.log

@Service
class UserServiceImpl @Autowired constructor(val passwordEncoder: PasswordEncoder,
                                             val userRepository: UserRepository,
                                             val authenticationManager: AuthenticationManager,
                                             val jwtProvider: JwtProvider) : UserService {

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

            UserRegisterResponse(true, "user ${user.username} registered successful");
        } catch (e: Exception) {
            UserRegisterResponse(false, "an error has occurred" + e.message);
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
            val userData=userRepository.findUserByUsername(userTokenRequest.username)
            val listOfMedications= mutableListOf<Medications>()
            val listOfReports= mutableListOf<Report>()
            val listOfPreservations= mutableListOf<Preservations>()
            val listOfNotifications= mutableListOf<Notifications>()
            val listOfSuggestions= mutableListOf<Suggestions>()
            for (medicine in userData.medications) {
                val user=MedicineUser(medicine.user.id,medicine.user.username,medicine.user.phone,medicine.user.fullName,medicine.user.pictureUrl)
                val category=MedicineCategory(medicine.category.id,medicine.category.name,medicine.category.imageUrl)
                val preserver=Preservations(medicine.preservation.id,medicine.preservation.timestamp)
                listOfMedications.add( Medications(medicine.id,medicine.name,medicine.lang,medicine.lat,medicine.imageUrl,medicine.addressTitle,user,category,preserver))
            }
            for (report in userData.reports) {
                listOfReports.add(Report(report.id,report.payload,report.timestamp))
            }

            for (notification in userData.notifications) {
                listOfNotifications.add(Notifications(notification.id,notification.title,notification.body,notification.timestamp))
            }
            for (suggestion in userData.suggestions) {
                listOfSuggestions.add(Suggestions(suggestion.id,suggestion.type,suggestion.timestamp,suggestion.title,suggestion.body))
            }
            UserProfileResponse(userData.id,userData.phone,userData.fullName,userData.pictureUrl,listOfMedications,listOfReports,listOfSuggestions,listOfNotifications)
        } catch (ex: Exception) {
            throw Exception(ex)
        }
    }


}