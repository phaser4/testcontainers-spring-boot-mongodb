package net.phaser4.testcontainers.mongodb

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "embedded.mongodb")
class MongoDbProperties {
    var dockerImage = "mongo:3.7.9"
    var defaultDatabase = "test"
}