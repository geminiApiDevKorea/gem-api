package com.jyami.gemapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class GemApiApplication

fun main(args: Array<String>) {
    runApplication<GemApiApplication>(*args)
}
