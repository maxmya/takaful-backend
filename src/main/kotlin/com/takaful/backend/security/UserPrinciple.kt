package com.takaful.backend.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.takaful.backend.data.entites.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class UserPrinciple(val user: User)
    : UserDetails {

    private var myCustomAuthorities: MutableCollection<out GrantedAuthority>


    init {
        val mAuth: MutableCollection<GrantedAuthority> = mutableListOf()
        mAuth.add(SimpleGrantedAuthority("ROLE_USER"))
        myCustomAuthorities = mAuth
    }


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return myCustomAuthorities
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun getPassword(): String {
        return user.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}