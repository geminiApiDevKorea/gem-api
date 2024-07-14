package com.jyami.gemapi.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import com.jyami.gemapi.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter


class FirebaseTokenFilter(
    private val userService: UserService,
    private val firebaseAuth: FirebaseAuth
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val decodedToken: FirebaseToken
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            setUnauthorizedResponse(response, "INVALID_HEADER")
            return
        }
        val token = header.substring(7)


        // verify IdToken
        try {
            decodedToken = firebaseAuth.verifyIdToken(token)
        } catch (e: FirebaseAuthException) {
            setUnauthorizedResponse(response, "INVALID_TOKEN")
            return
        }

        // User를 가져와 SecurityContext에 저장한다.
        try {
            val user = userService.loadUserById(decodedToken.uid) ?: throw NoSuchElementException("user not found")
            val authentication = UsernamePasswordAuthenticationToken(
                user, null, user?.authorities
            )
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: NoSuchElementException) {
            setUnauthorizedResponse(response, "USER_NOT_FOUND")
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun setUnauthorizedResponse(response: HttpServletResponse, code: String) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json"
        response.writer.write("{\"code\":\"$code\"}")
    }
}
