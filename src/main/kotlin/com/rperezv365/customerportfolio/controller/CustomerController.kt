package com.rperezv365.customerportfolio.controller

import com.rperezv365.customerportfolio.dto.CustomerInformation
import com.rperezv365.customerportfolio.dto.StockTradeRequest
import com.rperezv365.customerportfolio.dto.StockTradeResponse
import com.rperezv365.customerportfolio.service.CustomerService
import com.rperezv365.customerportfolio.service.TradeService
import mu.KLogging
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerService: CustomerService,
    private val tradeService: TradeService
) {

    companion object : KLogging()

    @GetMapping("/{customerId}")
    fun getCustomerInformation(@PathVariable customerId: String): Mono<CustomerInformation> {
        return customerService.getCustomerInformation(customerId)
    }

    @PostMapping("/{customerId}/trade")
    fun trade(@PathVariable customerId: String, @RequestBody mono: Mono<StockTradeRequest>) : Mono<StockTradeResponse> {

        logger.info { "trade service (in customer-service controller) was called!" }

        return mono.flatMap { request -> tradeService.trade(customerId, request) }
    }

}