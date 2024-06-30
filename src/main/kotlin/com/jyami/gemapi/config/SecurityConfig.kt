package com.jyami.gemapi.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
//import org.springframework.security.authentication.AuthenticationEventPublisher
//import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.security.provisioning.InMemoryUserDetailsManager

//@Configuration
//@EnableWebSecurity
//class SecurityConfig {
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
//
//}