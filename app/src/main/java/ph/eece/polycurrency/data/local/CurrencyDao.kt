package ph.eece.polycurrency.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ph.eece.polycurrency.data.local.entity.ExchangeRateEntity

@Dao
interface CurrencyDao {

    // 1. Get all rates. Return Flow so UI updates automatically when DB changes.
    @Query("SELECT * FROM exchange_rates")
    fun getAllRates(): Flow<List<ExchangeRateEntity>>

    // 2. Get a specific rate (for quick lookups)
    @Query("SELECT * FROM exchange_rates WHERE currencyCode = :code")
    suspend fun getRate(code: String): ExchangeRateEntity?

    // 3. Save new rates from API.
    // @Upsert replaces the row if the Primary Key (currencyCode) already exists.
    @Upsert
    suspend fun upsertRates(rates: List<ExchangeRateEntity>)
}