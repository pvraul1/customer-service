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

        /*
        // buy
        val buyRequest1 = StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY)
        trade("6841f71deb46c6922cf93e14", buyRequest1, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(9500)
            .jsonPath("$.totalPrice").isEqualTo(500)

        val buyRequest2 = StockTradeRequest(Ticker.GOOGLE, 100, 10, TradeAction.BUY)
        trade("6841f71deb46c6922cf93e14", buyRequest2, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(8500)
            .jsonPath("$.totalPrice").isEqualTo(1000)

        getCustomer("6841f71deb46c6922cf93e14", HttpStatus.OK)
            .jsonPath("$.holdings").isNotEmpty
            .jsonPath("$.holdings.length()").isEqualTo(1)
            .jsonPath("$.holdings[0].ticker").isEqualTo("GOOGLE")
            .jsonPath("$.holdings[0].quantity").isEqualTo(15)

        // sell
        val sellRequest1 = StockTradeRequest(Ticker.GOOGLE, 110, 5, TradeAction.SELL)
        trade("6841f71deb46c6922cf93e14", sellRequest1, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(9050)
            .jsonPath("$.totalPrice").isEqualTo(550)
        */

        val sellRequest2 = StockTradeRequest(Ticker.GOOGLE, 110, 10, TradeAction.SELL)
        trade("6841f71deb46c6922cf93e14", sellRequest2, HttpStatus.OK)
            .jsonPath("$.balance").isEqualTo(10150)
            .jsonPath("$.totalPrice").isEqualTo(1100)

        getCustomer("6841f71deb46c6922cf93e14", HttpStatus.OK)
            .jsonPath("$.holdings").isNotEmpty
            .jsonPath("$.holdings.length()").isEqualTo(1)
            .jsonPath("$.holdings[0].ticker").isEqualTo("GOOGLE")
            .jsonPath("$.holdings[0].quantity").isEqualTo(0)
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