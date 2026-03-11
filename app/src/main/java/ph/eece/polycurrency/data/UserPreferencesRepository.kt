package ph.eece.polycurrency.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val ACTIVE_CURRENCIES_KEY = stringSetPreferencesKey("active_currencies")
        val TARGET_CURRENCY_KEY = stringPreferencesKey("target_currency")
        val BASE_CURRENCY_KEY = stringPreferencesKey("base_currency")

        // Set the default upon installation
        val DEFAULT_CURRENCIES = setOf("USD", "EUR", "JPY", "GBP", "CNY")
        const val DEFAULT_TARGET = "USD"
        const val DEFAULT_BASE = "USD"
    }

    // Active Currency
    val activeCurrenciesFlow: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            preferences[ACTIVE_CURRENCIES_KEY]?.toList() ?: DEFAULT_CURRENCIES.toList()
        }

    suspend fun saveActiveCurrencies(currencies: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[ACTIVE_CURRENCIES_KEY] = currencies.toSet()
        }
    }

    // Target Currency
    val targetCurrencyFlow: Flow<String> = context.dataStore.data
        .map { it[TARGET_CURRENCY_KEY] ?: DEFAULT_TARGET }

    suspend fun saveTargetCurrency(code: String) {
        context.dataStore.edit { it[TARGET_CURRENCY_KEY] = code }
    }

    // Base Currency
    val baseCurrencyFlow: Flow<String> = context.dataStore.data
        .map { it[BASE_CURRENCY_KEY] ?: DEFAULT_BASE }
    suspend fun saveBaseCurrency(code: String) {
        context.dataStore.edit { it[BASE_CURRENCY_KEY] = code }
    }
}