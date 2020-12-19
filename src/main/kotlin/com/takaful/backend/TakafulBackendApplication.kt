package com.takaful.backend

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.takaful.backend.utils.FileStorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.FileInputStream

import java.io.IOException


@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties::class)
@EnableScheduling
class TakafulBackendApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<TakafulBackendApplication>(*args)
}


fun configure(application: SpringApplicationBuilder)
        : SpringApplicationBuilder {
    return application.sources(TakafulBackendApplication::class.java)
}

