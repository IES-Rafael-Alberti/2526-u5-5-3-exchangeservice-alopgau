package com.example.exchange

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.exactly
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.iesra.revilofe.ExchangeRateProvider
import org.iesra.revilofe.ExchangeService
import org.iesra.revilofe.InMemoryExchangeRateProvider
import org.iesra.revilofe.Money

class BusquedaTasas: DescribeSpec({

    afterTest {
        clearAllMocks()
    }

    describe("Tests evaluación busqueda tasas ") {
    /* Mock en todo este area ya que verifico orden de llamadas */
        val mock = mockk<InMemoryExchangeRateProvider>()
        val service = ExchangeService(mock)

        it("éxito en consulta directa") {
            every { mock.rate("USDEUR") } returns 0.92
            service.exchange(Money(100, "USD"), "EUR") shouldBe 92
            verifySequence { mock.rate("USDEUR") }
        }
        it("fallo en consulta directa y éxito en primer cruce válido") {
            every { mock.rate("USDEUR") } throws IllegalArgumentException()
            every { mock.rate("USDGBP") } returns 1.1
            every { mock.rate("GBPEUR") } returns 0.9

            service.exchange(Money(100, "USD"), "EUR") shouldBe (100 * 1.1 * 0.9).toLong()

            verifySequence {
                mock.rate("USDEUR")
                mock.rate("USDGBP")
                mock.rate("GBPEUR")
            }

        }
        it("fallo en primer cruce y éxito en un cruce alternativo posterior") {
            every { mock.rate("USDEUR") } throws IllegalArgumentException()
            every { mock.rate("USDGBP") } throws IllegalArgumentException()
            every { mock.rate("USDJPY") } returns 3.0
            every { mock.rate("JPYEUR") } returns 0.6

            service.exchange(Money(100, "USD"), "EUR") shouldBe (100 * 3.0 * 0.6).toLong()

            verifyOrder {
                mock.rate("USDEUR")
                mock.rate("USDGBP")
                mock.rate("USDJPY")
                mock.rate("JPYEUR")
            }


        }
        it("Fallo en todas las consultas") {
            every { mock.rate("USDEUR") } throws IllegalArgumentException()
            every { mock.rate("USDGBP") } throws IllegalArgumentException()
            every { mock.rate("GBPEUR") } throws IllegalArgumentException()
            shouldThrow<IllegalArgumentException> {
            service.exchange(Money(100, "USD"), "EUR")

            }

            verifyOrder {
                mock.rate("USDEUR")
                mock.rate("USDGBP")
                mock.rate("GBPEUR")
                mock.rate("USDJPY")
                mock.rate("JPYEUR")
            }
        }
    }

})

