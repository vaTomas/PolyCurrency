package ph.eece.polycurrency.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ph.eece.polycurrency.data.local.entity.ExchangeRateEntity

@Database(
    entities = [ExchangeRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // Tell the database about your DAO
    abstract val currencyDao: CurrencyDao
}