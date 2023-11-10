package ru.quipy.config

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class ProjectionConfig {
    @Autowired
    private lateinit var properties: ProjectionDatabaseProperties

    @PostConstruct
    fun setup() {
        val flywayConfig = with(properties) {
            val config = ClassicConfiguration()
            config.setDataSource(url, username, password)
            return@with config
        }

        Flyway(flywayConfig).migrate()

        with(properties) {
            Database.connect(url = url, driver = driver, user = username, password = password)
        }
    }
}


@ConstructorBinding
@ConfigurationProperties("projection.database")
data class ProjectionDatabaseProperties(
    val url: String,
    val driver: String,
    val username: String,
    val password: String,
)