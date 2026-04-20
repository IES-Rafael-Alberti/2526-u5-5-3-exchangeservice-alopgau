package com.example.exchange

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.iesra.revilofe.ExchangeRateProvider
import org.iesra.revilofe.ExchangeService
import org.iesra.revilofe.InMemoryExchangeRateProvider
import org.iesra.revilofe.Money

class ValidacionEntrada: DescribeSpec({

    afterTest {
        clearAllMocks()
    }

    describe("battery designed from equivalence classes for ExchangeService") {

        describe("input validation") {
            /* Mock a modo de stub para checkear validaciones en general*/
            val provider = mockk<ExchangeRateProvider>()
            val service = ExchangeService(provider)



            it("Cantidad de dinero es 0") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(0, "USD"), "EUR")
                }
            }

            it("Cantidad de dinero negativa") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(-1, "USD"), "EUR")
                }

            }

            it("Moneda origen invalida por longitud") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(1, "AAAA"), "EUR")
                }

            }

            it("Moneda destino invalida por longitud") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(1, "USD"), "AAAA")
                }

            }
            it("Moneda destino invalida (formato numerico)") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(1, "USD"), "123")
                }

            }
            it("Moneda origen invalida (formato numerico)") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(1, "123"), "USD")
                }

            }
            it("Comprobar si es case-sensitive") {
                every { provider.rate("USDEUR") } returns 0.92
                shouldNotThrowAny {
                    service.exchange(Money(1, "usd"), "eur")
                }

            }
        }

       //..
}})
