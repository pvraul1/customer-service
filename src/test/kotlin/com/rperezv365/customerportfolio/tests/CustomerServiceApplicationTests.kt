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

        // buy
        var buyRequest = StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY)
        trade("683b03f901873864a5f80d7b", buyRequest, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(9500)

    }

    @Test
    fun customerInformation() {
        getCustomer("683b03f901873864a5f80d79", HttpStatus.OK)
            .jsonPath("$.name").isEqualTo("Sam")
            .jsonPath("$.balance").isEqualTo(10000)
            .jsonPath("$.holdings").isEmpty
    }

    fun getCustomer(customerId: String, expectedStatus: HttpStatus): WebTestClient.BodyContentSpec {

        return client.get()
            .uri("/api/v1/customers/{cusotmerId}", customerId)
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