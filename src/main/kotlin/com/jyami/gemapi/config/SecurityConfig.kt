package com.jyami.gemapi.config

import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class SecurityConfig(
    val userDetailsService: UserDetailsService,
    val firebaseAuth: FirebaseAuth
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeRequests()
            .anyRequest().authenticated().and()
            .addFilterBefore(FirebaseTokenFilter(userDetailsService, firebaseAuth), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling{ it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))}
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring().requestMatchers(HttpMethod.POST, "/users")
                .requestMatchers("/")
                .requestMatchers("/resource/**")
                .requestMatchers("/hello")
        }
    }

//    @Bean
//    @ConditionalOnMissingBean(UserDetailsService::class)
//    fun inMemoryUserDetailsManager(): InMemoryUserDetailsManager {
//        val generatedPassword = "f98d2e30-026a-40ac-a9a0-605a1e488b74"
//        return InMemoryUserDetailsManager(
//            User.withUsername("user")
//                .password(generatedPassword)
//                .roles("USER")
//                .build()
//        );
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(AuthenticationEventPublisher::class)
//    fun defaultAuthenticationEventPublisher(delegate: ApplicationEventPublisher): DefaultAuthenticationEventPublisher {
//        return DefaultAuthenticationEventPublisher(delegate);
//    }

}