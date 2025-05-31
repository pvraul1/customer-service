package com.rperezv365.customerportfolio.exception

class InsufficientSharesException(id: String) : RuntimeException(
    "Customer [id=$id] does not have enough shares to complete the transaction"
)