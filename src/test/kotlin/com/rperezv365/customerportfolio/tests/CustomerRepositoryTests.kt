package com.rperezv365.customerportfolio.tests

import com.rperezv365.customerportfolio.domain.Ticker
import com.rperezv365.customerportfolio.entity.PortfolioItem
import com.rperezv365.customerportfolio.repository.PortfolioItemRepository
import mu.KLogging
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.SignalType
import java.util.function.Consumer


@SpringBootTest
class CustomerRepositoryTests {

    companion object : KLogging()

    @Autowired
    lateinit private var portfolioItemRepository: PortfolioItemRepository



    @Test
    fun findByCustomerAndTikerTest() {

        val startTime = System.currentTimeMillis()

        portfolioItemRepository.findByCustomerIdAndTicker(ObjectId("6841f71deb46c6922cf93e14"), Ticker.GOOGLE)
            .doOnSubscribe(Consumer { sub: Subscription? -> println("📥 Suscripción iniciada") })
            .doOnNext(Consumer { item: PortfolioItem? -> println("📦 Item recibido: " + item) })
            .doOnSuccess(Consumer { item: PortfolioItem? -> println("✅ Éxito con item: " + item) })
            .doOnError(Consumer { err: Throwable? -> System.err.println("❌ Error: " + err) })
            .doFinally { signalType: SignalType? ->
                val duration = System.currentTimeMillis() - startTime
                println("⏱️ Tiempo total: " + duration + "ms con señal: " + signalType)
            }
            .log()
            .subscribe()



    }
}