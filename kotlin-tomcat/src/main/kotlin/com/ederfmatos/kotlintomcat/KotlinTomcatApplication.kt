package com.ederfmatos.kotlintomcat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinTomcatApplication

fun main(args: Array<String>) {
    runApplication<KotlinTomcatApplication>(*args)
}
