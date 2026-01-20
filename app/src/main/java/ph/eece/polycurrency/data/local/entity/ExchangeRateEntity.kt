package ph.eece.polycurrency.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey
    val currencyCode: String,  // e.g., "USD", "EUR", "PHP"

    val rateRelativeToBase: Double, // e.g., 58.50 (if Base is USD and this is PHP)

    val lastUpdatedTimestamp: Long // For the "Offline Warning" logic
)