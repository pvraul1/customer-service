package com.rperezv365.customerportfolio.repository

import com.rperezv365.customerportfolio.entity.Customer
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRespository : ReactiveMongoRepository<Customer, String> {
}