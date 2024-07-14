package com.jyami.gemapi.config

import com.google.firebase.auth.FirebaseAuth
import com.jyami.gemapi.auth.FirebaseTokenFilter
import com.jyami.gemapi.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    val userService: UserService,
    val firebaseAuth: FirebaseAuth
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeRequests()
            .anyRequest().authenticated().and()
            .addFilterBefore(
                FirebaseTokenFilter(userService, firebaseAuth),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .csrf{csrf -> csrf.disable()}
            .exceptionHandling { it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) }
        return http.build()
    }

    val PERMIT_DOCS_URL_ARRAY: Array<String> = arrayOf(
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",  /* swagger v3 */
        "/api-docs/**",
        "/swagger-ui/**",
    )

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring().requestMatchers(HttpMethod.POST, "/users")
                .requestMatchers(*PERMIT_DOCS_URL_ARRAY)
                .requestMatchers("/resource/**")
                .requestMatchers("/hello")
        }
    }

}
