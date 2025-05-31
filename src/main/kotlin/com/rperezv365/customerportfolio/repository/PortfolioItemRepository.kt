package com.rperezv365.customerportfolio.repository

import com.rperezv365.customerportfolio.entity.PortfolioItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PortfolioItemRepository : ReactiveMongoRepository<PortfolioItem, String> {

    fun findByCustomerId(customerId: ObjectId): List<PortfolioItem>

}