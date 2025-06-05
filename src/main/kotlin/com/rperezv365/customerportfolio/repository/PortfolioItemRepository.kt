package com.rperezv365.customerportfolio.repository

import com.rperezv365.customerportfolio.domain.Ticker
import com.rperezv365.customerportfolio.entity.PortfolioItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface PortfolioItemRepository : ReactiveMongoRepository<PortfolioItem, String> {

    fun findAllByCustomerId(customerId: ObjectId): Flux<PortfolioItem>

    fun findByCustomerIdAndTicker(customerId: ObjectId, ticker: Ticker): Mono<PortfolioItem>

}