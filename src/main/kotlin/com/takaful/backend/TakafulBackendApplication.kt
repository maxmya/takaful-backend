package com.takaful.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class TakafulBackendApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<TakafulBackendApplication>(*args)
}


fun configure(application: SpringApplicationBuilder)
        : SpringApplicationBuilder {
    return application.sources(TakafulBackendApplication::class.java)
}

