package net.phaser4.testcontainers.mongodb

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TestConfiguration::class])
class MongoDbTestContainersTest {

    @Autowired
    lateinit var mongoDbProperties: MongoDbProperties

    @Value("\${spring.data.mongodb.port}")
    var mongoDbContainerPort: Int = 0

    @Autowired
    lateinit var repository: TestRepository

    @Test
    fun `properties are available`() {
        assert(mongoDbProperties.dockerImage.contains("mongo"))
    }

    @Test
    fun `container properties are available`() {
        assert(mongoDbContainerPort > 0)
    }

    @Test
    fun `spring data is operational`() {
        val document = TestDocument(11, "hello")
        repository.save(document)

        assert(document == repository.findOne(11))
    }
}

@Configuration
@EnableAutoConfiguration
open class TestConfiguration

interface TestRepository: MongoRepository<TestDocument, Long>
data class TestDocument (@Id var id: Long = 0, var data: String = "")