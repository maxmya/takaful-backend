package com.takaful.backend.security

import com.takaful.backend.data.repos.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {


    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {

        if (userRepository.existsByUsername(username!!)) {

            val user = userRepository.findUserByUsername(username)

            return UserPrinciple(user)

        } else throw UsernameNotFoundException("no user matches the entered username")
    }
}