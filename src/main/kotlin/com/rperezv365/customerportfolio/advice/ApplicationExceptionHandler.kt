package com.rperezv365.customerportfolio.advice

import com.rperezv365.customerportfolio.exception.CustomerNotFoundException
import com.rperezv365.customerportfolio.exception.InsufficientBalanceException
import com.rperezv365.customerportfolio.exception.InsufficientSharesException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.net.URI

@ControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleException(ex: CustomerNotFoundException): ProblemDetail {
        return build(HttpStatus.NOT_FOUND, ex) { problem ->
            problem.type = URI.create("http://example.com/problems/customer-not-found")
            problem.title = "Customer Not Found"
        }
    }

    @ExceptionHandler(InsufficientBalanceException::class)
    fun handleException(ex: InsufficientBalanceException): ProblemDetail {
        return build(HttpStatus.BAD_REQUEST, ex) { problem ->
            problem.type = URI.create("http://example.com/problems/insufficient-balance")
            problem.title = "Insufficient Balance"
        }
    }

    @ExceptionHandler(InsufficientSharesException::class)
    fun handleException(ex: InsufficientSharesException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)

        return build(HttpStatus.BAD_REQUEST, ex) { problem ->
            problem.type = URI.create("http://example.com/problems/insufficient-shares")
            problem.title = "Insufficient Shares"
        }
    }

    private fun build(status: HttpStatus, ex: Exception, consumer: (ProblemDetail) -> Unit): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(status, ex.message)
        consumer(problem)

        return problem
    }
}