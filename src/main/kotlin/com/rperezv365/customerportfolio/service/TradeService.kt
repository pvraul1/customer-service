package com.rperezv365.customerportfolio.service

import com.rperezv365.customerportfolio.domain.TradeAction
import com.rperezv365.customerportfolio.dto.StockTradeRequest
import com.rperezv365.customerportfolio.dto.StockTradeResponse
import com.rperezv365.customerportfolio.entity.Customer
import com.rperezv365.customerportfolio.entity.PortfolioItem
import com.rperezv365.customerportfolio.exception.ApplicationExceptions
import com.rperezv365.customerportfolio.mapper.EntityDtoMapper
import com.rperezv365.customerportfolio.repository.CustomerRespository
import com.rperezv365.customerportfolio.repository.PortfolioItemRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class TradeService(
    private val customerRepository: CustomerRespository,
    private val portfolioItemRepository: PortfolioItemRepository
) {

    @Transactional
    fun trade(customerId: String, request: StockTradeRequest): Mono<StockTradeResponse> {
        return when (request.action) {
            TradeAction.BUY -> this.buyStock(customerId, request)
            TradeAction.SELL -> this.sellStock(customerId, request)
        }
    }

    private fun TradeService.buyStock(customerId: String, request: StockTradeRequest): Mono<StockTradeResponse> {
        val customerMono = customerRepository.findById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
            .filter { customer -> customer.balance >= request.totalPrice() }
            .switchIfEmpty(ApplicationExceptions.insufficientBalance(customerId))

        val portfolioItemMono = portfolioItemRepository.findByCustomerIdAndTicker(ObjectId(customerId), request.ticker)
            .defaultIfEmpty(EntityDtoMapper.toPortfolioItem(customerId, request.ticker))

        return customerMono.zipWhen{ customer -> portfolioItemMono }
            .flatMap { it -> this.executeBuy(it.t1, it.t2, request) }
    }

    private fun TradeService.sellStock(customerId: String, request: StockTradeRequest): Mono<StockTradeResponse> {
        val customerMono = customerRepository.findById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))

        val portfolioItemMono = portfolioItemRepository.findByCustomerIdAndTicker(ObjectId(customerId), request.ticker)
            .filter { item -> item.quantity >= request.quantity }
            .switchIfEmpty(ApplicationExceptions.insufficientShares(customerId))

        return customerMono.zipWhen{ customer -> portfolioItemMono }
            .flatMap { it -> this.executeSell(it.t1, it.t2, request) }
    }

    private fun executeBuy(customer: Customer, portfolioItem: PortfolioItem, request: StockTradeRequest): Mono<StockTradeResponse> {
        customer.balance -= request.totalPrice()
        portfolioItem.quantity += request.quantity

        return this.saveAndBuildResponse(customer, portfolioItem, request)
    }

    private fun executeSell(customer: Customer, portfolioItem: PortfolioItem, request: StockTradeRequest): Mono<StockTradeResponse> {
        customer.balance += request.totalPrice()
        portfolioItem.quantity -= request.quantity

        return this.saveAndBuildResponse(customer, portfolioItem, request)
    }

    private fun saveAndBuildResponse(
        customer: Customer,
        portfolioItem: PortfolioItem,
        request: StockTradeRequest
    ): Mono<StockTradeResponse> {

        val response = EntityDtoMapper.toStockTradeResponse(request, customer.id, customer.balance)

        return Mono.zip(
            customerRepository.save(customer),
            portfolioItemRepository.save(portfolioItem)
        ).map { response }

    }
}




