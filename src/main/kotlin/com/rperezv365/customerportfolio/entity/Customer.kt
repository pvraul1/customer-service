package com.rperezv365.customerportfolio.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "customers")
data class Customer(

    @Id var id: String? = null,
    var name: String,
    var balance: Int

)
