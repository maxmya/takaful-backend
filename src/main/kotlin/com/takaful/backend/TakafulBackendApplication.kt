package com.takaful.backend

import com.takaful.backend.utils.FileStorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties::class)
class TakafulBackendApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<TakafulBackendApplication>(*args)
}


fun configure(application: SpringApplicationBuilder)
        : SpringApplicationBuilder {
    return application.sources(TakafulBackendApplication::class.java)
}

