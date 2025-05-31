package com.rperezv365.customerportfolio.exception

import reactor.core.publisher.Mono

object ApplicationExceptions {

    fun <T> customerNotFound(customerId: String): Mono<T> {
        return Mono.error(CustomerNotFoundException(customerId))
    }

    fun <T> insufficientBalance(customerId: String): Mono<T> {
        return Mono.error(InsufficientBalanceException(customerId))
    }

    fun <T> insufficientShares(customerId: String): Mono<T> {
        return Mono.error(InsufficientSharesException(customerId))
    }

}