package com.jyami.gemapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GemApiApplication

fun main(args: Array<String>) {
    runApplication<GemApiApplication>(*args)
}
