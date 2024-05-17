package com.hrblizz.fileapi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.util.TimeZone
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.hrblizz.fileapi.data.repository"])
@EnableMongoAuditing
class Application : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(Application::class.java)
    }

    @PostConstruct
    fun setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
