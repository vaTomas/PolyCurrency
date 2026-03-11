package ph.eece.polycurrency.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface FrankfurterApi {

    // 1. Get the names (e.g., "USD": "United States Dollar")
    @GET("v1/currencies")
    suspend fun getCurrencies(): Map<String, String>

    // 2. Get the math rates (e.g., base=PHP)
    @GET("v1/latest")
    suspend fun getLatestRates(
        @Query("base") base: String
    ): FrankfurterLatestResponse

    companion object {
        const val BASE_URL = "https://api.frankfurter.dev/"
    }
}