package com.rperezv365.customerportfolio.dto

import com.rperezv365.customerportfolio.domain.Ticker

data class Holding(

    val ticker: Ticker,
    val quantity: Int

)
