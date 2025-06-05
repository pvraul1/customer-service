package com.rperezv365.customerportfolio.service

import com.rperezv365.customerportfolio.dto.CustomerInformation
import com.rperezv365.customerportfolio.entity.Customer
import com.rperezv365.customerportfolio.exception.ApplicationExceptions
import com.rperezv365.customerportfolio.mapper.EntityDtoMapper
import com.rperezv365.customerportfolio.repository.CustomerRespository
import com.rperezv365.customerportfolio.repository.PortfolioItemRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomerService(
    private val customerRespository: CustomerRespository,
    private val portfolioItemRepository: PortfolioItemRepository
) {

    fun getCustomerInformation(customerId: String): Mono<CustomerInformation> {

        return customerRespository.findById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
            .flatMap(this::buildCustomerInformation)
            .log()

    }

    fun buildCustomerInformation(customer: Customer): Mono<CustomerInformation> {
        return portfolioItemRepository.findAllByCustomerId(ObjectId(customer.id))
            .collectList()
            .map { list -> EntityDtoMapper.toCustomerInformation(customer, list) }
    }

}