package com.rperezv365.customerportfolio.bootstrap

import com.rperezv365.customerportfolio.entity.Customer
import com.rperezv365.customerportfolio.repository.CustomerRespository
import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class DataInitializer(
    private val customerRepository: CustomerRespository
) : CommandLineRunner {

    companion object : KLogging()

    override fun run(vararg args: String?) {
        // Check if the database is empty
       customerRepository.count()
           .filter { it == 0L }
           .flatMap {
                logger.info("📥 Inserting initial data...")

                // create and save customers
                val sam = Customer(name = "Sam", balance = 10000)
                val mike = Customer(name = "Mike", balance = 10000)
                val john = Customer(name = "John", balance = 10000)

               // save customers
               Flux.just(sam, mike, john)
                   .flatMap { customerRepository.save(it) }
                   .log()
                   .then()
           }
           .doOnSuccess {
               logger.info("✅ Initial data inserted.")
           }
           .subscribe()
    }

}