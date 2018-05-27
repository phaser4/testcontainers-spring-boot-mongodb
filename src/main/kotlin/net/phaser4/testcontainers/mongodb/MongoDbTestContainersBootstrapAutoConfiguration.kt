package net.phaser4.testcontainers.mongodb

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

@Order(HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnProperty(name = ["mongodb.container.enabled"], matchIfMissing = true)
@EnableConfigurationProperties(MongoDbProperties::class)
open class MongoDbTestContainersBootstrapAutoConfiguration {

    @Autowired
    lateinit var mongoDbProperties: MongoDbProperties

    val log = LoggerFactory.getLogger(this::class.java)

    @Bean(name = ["mongoDbContainer"], destroyMethod = "stop")
    open fun mongoDbContainer(environment: ConfigurableEnvironment): MongoDbContainer {
        val image = mongoDbProperties.dockerImage
        log.info("Starting MongoDB in container; docker image is $image")

        val container = MongoDbContainer(image)
                .withEnv("MONGO_INITDB_DATABASE", mongoDbProperties.defaultDatabase)
        container.start()
        val port = container.getMappedPort(27017)
        log.info("Started MongoDB on port ${port}")

        registerContainerProperties(port, environment)
        return container
    }

    fun registerContainerProperties(port: Int,
                                    environment: ConfigurableEnvironment) {

        val properties = mapOf(
                "spring.data.mongodb.port" to port,
                "spring.data.mongodb.host" to "localhost",
                "spring.data.mongodb.database" to mongoDbProperties.defaultDatabase)

        val propertySource = MapPropertySource("mongoDbContainerProperties", properties)
        environment.getPropertySources().addFirst(propertySource)
    }
}