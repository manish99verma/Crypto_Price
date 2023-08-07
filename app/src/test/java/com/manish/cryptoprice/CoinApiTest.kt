package com.manish.cryptoprice

import com.google.gson.Gson
import com.manish.cryptoprice.data.api.CoinGeckoService
import com.manish.cryptoprice.data.model.chart.GraphValues
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinApiTest {
    lateinit var mockWebServer: MockWebServer
    lateinit var coinsApi: CoinGeckoService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        coinsApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CoinGeckoService::class.java)
    }

    @Test
    fun testGetCoinsList() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setBody("[]")
        mockWebServer.enqueue(mockResponse)

        val response = coinsApi.getCoinsList("usd", "market_cap_desc", 100, 1, "en")
        mockWebServer.takeRequest()

        Assert.assertEquals(true, response.body()!!.isEmpty())
    }

    @Test
    fun testGetCoinsList_returnsList() = runTest {
        val mockResponse = MockResponse()
        val content = JsonReaderHelper.readFileResource("/response.json");
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = coinsApi.getCoinsList("usd", "market_cap_desc", 100, 1, "en")
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.body()!!.isEmpty())
        Assert.assertEquals(100, response.body()!!.size)
    }

    @Test
    fun testGetCoinsList_returnsError() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something went wrong!")
        mockWebServer.enqueue(mockResponse)

        val response = coinsApi.getCoinsList("usd", "market_cap_desc", 100, 1, "en")
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.isSuccessful)
        Assert.assertEquals(404, response.code())
    }

    @Test
    fun testGetCoinsHistoryChart() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setBody("{}")
        mockWebServer.enqueue(mockResponse);

        val response = coinsApi.getHistoryChart("bitcoin", "usd", "1", "hourly")
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response!!.body()!!.id.isNullOrEmpty())
    }

    @Test
    fun testGetCoinsHistoryChart_returnsError() = runTest {
        val mockResponse = MockResponse();
        mockResponse.setBody("Something went wrong!")
        mockResponse.setResponseCode(404)
        mockWebServer.enqueue(mockResponse);

        val response = coinsApi.getHistoryChart("bitcoin", "usd", "1", "hourly")
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.isSuccessful)
        Assert.assertEquals(404, response.code())
    }

    @Test
    fun testGetCoinsHistoryChart_returns_chart() = runTest {
        val content = JsonReaderHelper.readFileResource("/response_get_365_yearly_history.json")
        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        val response = coinsApi.getHistoryChart("bitcoin", "usd", "365", "daily")
        mockWebServer.takeRequest()

        Assert.assertEquals(true, response.isSuccessful)
        Assert.assertEquals(200, response.code())
        Assert.assertEquals(365, response.body()!!.prices.size)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}