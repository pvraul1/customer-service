package com.rperezv365.customerportfolio.entity

import com.rperezv365.customerportfolio.domain.Ticker
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "portfolio_items")
data class PortfolioItem(

    @Id var id: String? = null,
    var customerId: ObjectId,
    var ticker: Ticker,
    var quantity: Int

)
