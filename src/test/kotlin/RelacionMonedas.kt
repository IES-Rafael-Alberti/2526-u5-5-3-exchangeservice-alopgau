package com.example.exchange
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
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
            val service = ExchangeService(providerSpy)

            it("Origen es igual a destino") {


            }


            it("Origen distinto de destino (tasa directa)") {


            }

            it("Origen distinto de destino (ruta cruzada)") {



            }

            it("Conversion imposible") {

            }

        }

})
