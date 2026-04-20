package com.example.exchange

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.iesra.revilofe.ExchangeRateProvider
import org.iesra.revilofe.ExchangeService
import org.iesra.revilofe.Money

class ValidacionEntrada: DescribeSpec({

    afterTest {
        clearAllMocks()
    }

    describe("battery designed from equivalence classes for ExchangeService") {

        describe("input validation") {
            val provider = mockk<ExchangeRateProvider>()
            val service = ExchangeService(provider)

            it("throws an exception when the amount is zero") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(0, "USD"), "EUR")
                }
            }

            it("throws an exception when the amount is negative") {
            shouldThrow<IllegalArgumentException> {
                service.exchange(Money(-1,"USD"),"EUR")
            }

            }

            it("throws an exception when the source currency code is invalid") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(1,"AAAA"),"EUR")
                }

            }

            it("throws an exception when the target currency code is invalid") {
                shouldThrow<IllegalArgumentException> {
                    service.exchange(Money(-1,"USD"),"AAAA")
                }
            }

        }

       //..
}})
