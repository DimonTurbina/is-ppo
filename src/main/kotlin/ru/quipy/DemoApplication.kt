package ru.quipy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("ru.quipy.config")
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
