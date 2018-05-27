package net.phaser4.testcontainers.mongodb

import org.testcontainers.containers.GenericContainer

class MongoDbContainer(image: String): GenericContainer<MongoDbContainer>(image)