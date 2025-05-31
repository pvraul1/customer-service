package com.rperezv365.customerportfolio.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration

@Configuration
class MongoConfig : AbstractReactiveMongoConfiguration() {

    override fun getDatabaseName(): String = "portfoliodb"

    @Bean
    override fun reactiveMongoClient(): MongoClient = MongoClients.create("mongodb://localhost:27017")

}