package com.takaful.backend.utils

import org.springframework.stereotype.Component

/**
 * @author Mahmoud.Aref - maxmya
 * Created At: 4/4/2019
 */
@Component
class HeadersParser {
    fun parseToken(bearer: String?): String {
        return if (bearer != null)
            if (bearer.contains("Bearer"))
                bearer.replace("Bearer ", "")
            else bearer.replace("bearer ", "")
        else ""
    }
}