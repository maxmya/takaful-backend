package com.takaful.backend.security

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Transactional
@Component
class JwtProvider {


    @Value("\${jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${jwtExpiration}")
    private val jwtExpiration: Int = 0

    fun generateJwtToken(authentication: Authentication): String {

        val userPrincipal = authentication.principal as UserPrinciple

        return if (jwtSecret != null) {
            Jwts.builder()
                    .setSubject(userPrincipal.username)
                    .setClaims(mapOf(
                            Pair("iat", Date()),
                            Pair("name", userPrincipal.user.fullName),
                            Pair("exp", Date().time + jwtExpiration),
                            Pair("username", userPrincipal.username)))
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact()
        } else {
            ""
        }
    }


    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body["username"].toString()
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            e.printStackTrace()

        } catch (e: MalformedJwtException) {
            e.printStackTrace()

        } catch (e: ExpiredJwtException) {
            e.printStackTrace()

        } catch (e: UnsupportedJwtException) {
            e.printStackTrace()

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()

        }

        return false
    }


}