package com.rperezv365.customerportfolio.controller

import com.rperezv365.customerportfolio.dto.CustomerInformation
import com.rperezv365.customerportfolio.dto.StockTradeRequest
import com.rperezv365.customerportfolio.dto.StockTradeResponse
import com.rperezv365.customerportfolio.service.CustomerService
import com.rperezv365.customerportfolio.service.TradeService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController(
    private val customerService: CustomerService,
    private val tradeService: TradeService
) {

    @GetMapping("/{customer}")
    fun getCustomerInformation(@PathVariable customerId: String): Mono<CustomerInformation> {
        return customerService.getCustomerInformation(customerId)
    }

    @PostMapping("/{cusotmerId}/trade")
    fun trade(@PathVariable customerId: String, @RequestBody mono: Mono<StockTradeRequest>) : Mono<StockTradeResponse> {
        return mono.flatMap { request -> tradeService.trade(customerId, request) }
    }

}