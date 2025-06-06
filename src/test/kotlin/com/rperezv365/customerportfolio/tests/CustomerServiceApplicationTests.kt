package com.rperezv365.customerportfolio.tests

import com.rperezv365.customerportfolio.domain.Ticker
import com.rperezv365.customerportfolio.domain.TradeAction
import com.rperezv365.customerportfolio.dto.StockTradeRequest
import mu.KLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerServiceApplicationTests {

    companion object : KLogging()

    @Autowired
    lateinit private var client: WebTestClient

    @Test
    fun buyAndShell() {
        val customerId = "6842f8cb7f09348aa874ee91"
        // buy
        val buyRequest1 = StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY)
        trade(customerId, buyRequest1, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(9500)
            .jsonPath("$.totalPrice").isEqualTo(500)

        val buyRequest2 = StockTradeRequest(Ticker.GOOGLE, 100, 10, TradeAction.BUY)
        trade(customerId, buyRequest2, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(8500)
            .jsonPath("$.totalPrice").isEqualTo(1000)

        getCustomer(customerId, HttpStatus.OK)
            .jsonPath("$.holdings").isNotEmpty
            .jsonPath("$.holdings.length()").isEqualTo(1)
            .jsonPath("$.holdings[0].ticker").isEqualTo("GOOGLE")
            .jsonPath("$.holdings[0].quantity").isEqualTo(15)

        // sell
        val sellRequest1 = StockTradeRequest(Ticker.GOOGLE, 110, 5, TradeAction.SELL)
        trade(customerId, sellRequest1, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(9050)
            .jsonPath("$.totalPrice").isEqualTo(550)

        val sellRequest2 = StockTradeRequest(Ticker.GOOGLE, 110, 10, TradeAction.SELL)
        trade(customerId, sellRequest2, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(10150)
            .jsonPath("$.totalPrice").isEqualTo(1100)

        getCustomer(customerId, HttpStatus.OK)
            .jsonPath("$.holdings").isNotEmpty
            .jsonPath("$.holdings.length()").isEqualTo(1)
            .jsonPath("$.holdings[0].ticker").isEqualTo("GOOGLE")
            .jsonPath("$.holdings[0].quantity").isEqualTo(0)
    }

    @Test
    fun customerNotFound() {
        val customerId = "6842f8cb7f09348aa874ee94"
        getCustomer(customerId, HttpStatus.NOT_FOUND)
            .jsonPath("$.detail").isEqualTo("Customer [id=$customerId] is not found")

        val sellRequest = StockTradeRequest(Ticker.GOOGLE, 110, 5, TradeAction.SELL)
        trade(customerId, sellRequest, HttpStatus.NOT_FOUND)
            .jsonPath("$.detail").isEqualTo("Customer [id=$customerId] is not found")
    }

    @Test
    fun insufficientBalance() {
        val customerId = "6842f8cb7f09348aa874ee93"
        val buyRequest = StockTradeRequest(Ticker.GOOGLE, 100, 101, TradeAction.BUY)
        trade(customerId, buyRequest, HttpStatus.BAD_REQUEST)
            .jsonPath("$.detail").isEqualTo("Customer [id=$customerId] does not have enough funds to complete the transaction")
    }

    @Test
    fun insufficientShares() {
        val customerId = "6842f8cb7f09348aa874ee93"
        val sellRequest = StockTradeRequest(Ticker.GOOGLE, 100, 1, TradeAction.SELL)
        trade(customerId, sellRequest, HttpStatus.BAD_REQUEST)
            .jsonPath("$.detail").isEqualTo("Customer [id=$customerId] does not have enough shares to complete the transaction")
    }

    @Test
    fun customerInformation() {
        getCustomer("6842f8cb7f09348aa874ee93", HttpStatus.OK)
            .jsonPath("$.name").isEqualTo("John")
            .jsonPath("$.balance").isEqualTo(10000)
            .jsonPath("$.holdings").isEmpty
    }

    fun getCustomer(customerId: String, expectedStatus: HttpStatus): WebTestClient.BodyContentSpec {

        return client.get()
            .uri("/api/v1/customers/{customerId}", customerId)
            .exchange()
            .expectStatus().isEqualTo(expectedStatus)
            .expectBody()
            .consumeWith { body ->
                val responseBody = body.responseBody
                val message = responseBody?.toString(Charsets.UTF_8) ?: "Empty response"
                logger.info(message)
            }
    }

    fun trade(customerId: String, request: StockTradeRequest, expectedStatus: HttpStatus): WebTestClient.BodyContentSpec {

        return client.post()
            .uri("/api/v1/customers/{cusotmerId}/trade", customerId)
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(expectedStatus)
            .expectBody()
            .consumeWith { body ->
                val responseBody = body.responseBody
                val message = responseBody?.toString(Charsets.UTF_8) ?: "Empty response"
                logger.info(message)
            }
    }

}