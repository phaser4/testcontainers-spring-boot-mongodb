package net.phaser4.testcontainers.mongodb

import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
@AutoConfigureBefore(MongoDataAutoConfiguration::class)
@ConditionalOnClass(MongoTemplate::class)
@ConditionalOnProperty(name = ["mongodb.container.enabled"], matchIfMissing = true)
open class MongoDbTestContainersDependenciesConfiguration {

    @Configuration
    open class DependsOnPostProcessor : BeanFactoryPostProcessor {
        override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
            val beanNames = beanFactory.getBeanNamesForType(MongoTemplate::class.java)
            beanNames.forEach {
                val beanDefinition = beanFactory.getBeanDefinition(it)
                val dependsOn = beanDefinition.dependsOn ?: emptyArray()
                beanDefinition.setDependsOn(*dependsOn.plus("mongoDbContainer"))
            }
        }
    }
}