package com.rperezv365.customerportfolio.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "portfolio_items")
data class PortfolioItem(
    @Id var id: String? = null,
    var customerId: ObjectId,
    var ticket: String,
    var quantity: String
)
