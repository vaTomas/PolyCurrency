package ph.eece.polycurrency.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ph.eece.polycurrency.data.CurrencyRepository
import ph.eece.polycurrency.data.UserPreferencesRepository
import ph.eece.polycurrency.data.local.AppDatabase
import ph.eece.polycurrency.data.local.CurrencyDao
import ph.eece.polycurrency.data.remote.FrankfurterApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    // Remote Data Get API
    @Provides
    @Singleton
    fun provideFrankfurterApi(): FrankfurterApi {
        return Retrofit.Builder()
            .baseUrl(FrankfurterApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin Data Classes
            .build()
            .create(FrankfurterApi::class.java)
    }

    // Local Database ROOM
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "polycurrency_db"
        ).build()
    }

    // Data Access Object (Database Commands)
    @Provides
    @Singleton
    fun provideCurrencyDao(database: AppDatabase): CurrencyDao {
        return database.currencyDao
    }

    // Repository
    @Provides
    @Singleton
    fun provideCurrencyRepository(
        api: FrankfurterApi,
        dao: CurrencyDao
    ): CurrencyRepository {
        return CurrencyRepository(api, dao)
    }

    // Preferences User Settings
    @Provides
    @Singleton
    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }
}