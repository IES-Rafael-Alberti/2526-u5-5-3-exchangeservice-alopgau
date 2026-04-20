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
import io.mockk.verifySequence
import org.iesra.revilofe.ExchangeRateProvider
import org.iesra.revilofe.ExchangeService
import org.iesra.revilofe.InMemoryExchangeRateProvider
import org.iesra.revilofe.Money

class RelacionMonedas: DescribeSpec({

    afterTest {
        clearAllMocks()
    }

    describe("Tests correcto funcionamiento de la relacion entre moneda origen y destino ") {

            val realProvider = InMemoryExchangeRateProvider(mapOf(
                "USDEUR" to 0.92,
                "GBPJPY" to 0.83,
                "USDGBP" to 0.73
                ))
            val providerSpy = spyk(realProvider)
            val serviceSpy = ExchangeService(providerSpy)
            val providerMock = mockk<InMemoryExchangeRateProvider>()
            val serviceMock = ExchangeService(providerMock)

            it("Origen es igual a destino") {
                serviceSpy.exchange(Money(1000, "USD"),"USD") shouldBe 1000
                verify (exactly = 0) {providerSpy.rate("USDUSD")}
            }


            it("Origen distinto de destino (tasa directa)") {
                serviceMock.exchange(Money(1000, "USD"),"EUR") shouldBe 920
                verify (exactly = 1) {providerSpy.rate("USDEUR")}


            }

            it("Origen distinto de destino (ruta cruzada)") {
                serviceSpy.exchange(Money(1000, "USD"),"JPY") shouldBe 605.9.toLong()
                verify ( exactly = 5) {providerSpy.rate(any())}


            }
            it("Conversion imposible") {
                shouldThrow<IllegalArgumentException> {
                serviceSpy.exchange(Money(1000, "EUR"),"JPY")
                }

            }

        }

})
