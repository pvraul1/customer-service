package com.rperezv365.customerportfolio.mapper

import com.rperezv365.customerportfolio.dto.CustomerInformation
import com.rperezv365.customerportfolio.dto.Holding
import com.rperezv365.customerportfolio.entity.Customer
import com.rperezv365.customerportfolio.entity.PortfolioItem

object EntityDtoMapper {

    fun toCustomerInformation(customer: Customer, items: List<PortfolioItem>): CustomerInformation {
        val holdings = items.stream()
            .map { item -> Holding(item.ticker, item.quantity) }
            .toList()

        return CustomerInformation(customer.id, customer.name, customer.balance, holdings)
    }

}