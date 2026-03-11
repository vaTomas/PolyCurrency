package ph.eece.polycurrency.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import ph.eece.polycurrency.data.local.CurrencyDao
import ph.eece.polycurrency.data.local.entity.ExchangeRateEntity
import ph.eece.polycurrency.data.remote.FrankfurterApi

class CurrencyRepository(
    private val api: FrankfurterApi,
    private val dao: CurrencyDao
) {
    // 1. The UI observes this Flow. It never cares about the API directly.
    val allRates: Flow<List<ExchangeRateEntity>> = dao.getAllRates()

    // 2. The function to sync data from the Internet to the Local Database
    suspend fun syncRates(backendBase: String = "PHP") {
        try {
            // Fetch names and rates simultaneously
            val namesMap = api.getCurrencies()
            val latestData = api.getLatestRates(base = backendBase)

            val timestamp = System.currentTimeMillis()
            val entitiesToSave = mutableListOf<ExchangeRateEntity>()

            // Map the API data to our Database Entities
            latestData.rates.forEach { (code, rate) ->
                entitiesToSave.add(
                    ExchangeRateEntity(
                        currencyCode = code,
                        currencyName = namesMap[code] ?: "Unknown Currency",
                        rateRelativeToBase = rate,
                        lastUpdatedTimestamp = timestamp
                    )
                )
            }

            // IMPORTANT: The API doesn't return the base currency in the 'rates' list.
            // We must add our Backend Base (PHP) manually with a rate of 1.0!
            entitiesToSave.add(
                ExchangeRateEntity(
                    currencyCode = backendBase,
                    currencyName = namesMap[backendBase] ?: backendBase,
                    rateRelativeToBase = 1.0,
                    lastUpdatedTimestamp = timestamp
                )
            )

            // Save to Local Database (Upsert will overwrite old rates, keep new ones)
            dao.upsertRates(entitiesToSave)
            Log.d("Repository", "Successfully synced ${entitiesToSave.size} currencies.")

        } catch (e: Exception) {
            // If offline, the app just uses whatever is already in the Room DB.
            Log.e("Repository", "Failed to sync rates. User might be offline.", e)
        }
    }
}