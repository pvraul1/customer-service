package com.rperezv365.customerportfolio.mapper

import com.rperezv365.customerportfolio.domain.Ticker
import com.rperezv365.customerportfolio.dto.CustomerInformation
import com.rperezv365.customerportfolio.dto.Holding
import com.rperezv365.customerportfolio.dto.StockTradeRequest
import com.rperezv365.customerportfolio.dto.StockTradeResponse
import com.rperezv365.customerportfolio.entity.Customer
import com.rperezv365.customerportfolio.entity.PortfolioItem
import org.bson.types.ObjectId

object EntityDtoMapper {

    fun toCustomerInformation(customer: Customer, items: List<PortfolioItem>): CustomerInformation {
        val holdings = items.stream()
            .map { item -> Holding(item.ticker, item.quantity) }
            .toList()

        return CustomerInformation(customer.id, customer.name, customer.balance, holdings)
    }

    fun toPortfolioItem(customerId: String, ticker: Ticker): PortfolioItem {
        return PortfolioItem(
            null, ObjectId(customerId), ticker, 0
        )
    }

    fun toStockTradeResponse(request: StockTradeRequest, customerId: String?, balance: Int): StockTradeResponse {
        return StockTradeResponse(
            customerId,
            request.ticker,
            request.price,
            request.quantity,
            request.action,
            request.totalPrice(),
            balance
        )
    }

}