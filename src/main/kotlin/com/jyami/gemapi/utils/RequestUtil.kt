package com.jyami.gemapi.utils

import jakarta.servlet.http.HttpServletRequest


object RequestUtil {
    fun getAuthorizationToken(header: String?): String {
        // Authorization: Bearer <access_token>
        require(header != null  && header.startsWith("Bearer ")) { "Invalid authorization header" }
        // Remove Bearer from string
        header.replace("Bearer ", "")
        val parts = header.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(parts.size == 2) { "Invalid authorization header" }
        // Get token
        return parts[1]
    }

    fun getAuthorizationToken(request: HttpServletRequest): String {
        return getAuthorizationToken(request.getHeader("Authorization"))
    }
}
