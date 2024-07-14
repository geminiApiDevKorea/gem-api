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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(
    val userService: UserService,
    val firebaseAuth: FirebaseAuth
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorizeRequests ->
            authorizeRequests.requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(*PERMIT_DOCS_URL_ARRAY).permitAll()
                .requestMatchers("/resource/**").permitAll()
                .requestMatchers("/hello").permitAll()
        }
        .addFilterBefore(
            FirebaseTokenFilter(userService, firebaseAuth),
            UsernamePasswordAuthenticationFilter::class.java
        )
        .cors { corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()) }
        .csrf { csrf -> csrf.disable() }
        .exceptionHandling { it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
//        corsConfiguration.allowedOrigins = listOf("*") : domain 넣기.
        corsConfiguration.allowedOriginPatterns = listOf("*")
        corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        corsConfiguration.allowedHeaders = listOf("*")
        corsConfiguration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }

    companion object{
        private val PERMIT_DOCS_URL_ARRAY: Array<String> = arrayOf(
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",  /* swagger v3 */
            "/api-docs/**",
            "/swagger-ui/**",
        )
    }

}
