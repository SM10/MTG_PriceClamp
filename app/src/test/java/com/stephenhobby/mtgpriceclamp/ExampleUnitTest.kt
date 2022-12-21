package com.stephenhobby.mtgpriceclamp

import org.junit.Test
import org.junit.Assert.*
import com.stephenhobby.mtgpriceclamp.datatype.*
import com.stephenhobby.mtgpriceclamp.sql.ScryfallApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ScryfallApiTest{
    //values to change for testing purposes.
    val testString1 = "Dune Beetle"
    val testString2 = "Kamigawa"

    //To test the instantiation of cards through scryfall querying
    //Best used in tandem with scryfall.com for testing
    @Test
    fun CardCreationTest(){
        val scryfall = ScryfallApi(callbackTest(testString1))
        scryfall.UserQuery(testString1)
    }

    class callbackTest(val testString : String) : ScryfallApi.Callback{
        override fun onQueryResult(cardlist: ArrayList<Card>) {
            assert(cardlist.size > 0)
            for(card in cardlist)
            {
                assertEquals("Name Assertion", testString, card.name)
                assertEquals("Price Assertion", 0.15, card.prices?.get("usd_foil"))
                assertEquals("Price Assertion", Double.NaN, card.prices?.get("usd_etched"))
            }
        }

        override fun onQueryError(error: Error) {

            println(error.message)
            println(error.stackTrace)
            assert(false)
        }

    }
}

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}