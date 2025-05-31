package com.rperezv365.customerportfolio.dto

import com.rperezv365.customerportfolio.domain.Ticker
import com.rperezv365.customerportfolio.domain.TradeAction

data class StockTradeRequest(

    val ticker: Ticker,
    val price: Int,
    val quantity: Int,
    val action: TradeAction

)
