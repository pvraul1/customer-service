package com.rperezv365.customerportfolio.exception

class InsufficientBalanceException(id: String) : RuntimeException(
    "Customer [id=$id] does not have enough funds to complete the transaction"
)
